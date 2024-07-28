package uz.ecobin.ecobin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.config.JwtService;
import uz.ecobin.ecobin.dto.request.AuthenticationRequest;
import uz.ecobin.ecobin.dto.request.RegisterRequest;
import uz.ecobin.ecobin.dto.response.ApiResponse;
import uz.ecobin.ecobin.dto.response.AuthenticationResponse;
import uz.ecobin.ecobin.dto.response.ResetPasswordResponse;
import uz.ecobin.ecobin.exceptions.AlreadyExists;
import uz.ecobin.ecobin.exceptions.AuthenticationException;
import uz.ecobin.ecobin.exceptions.CommonException;
import uz.ecobin.ecobin.exceptions.NotFoundException;
import uz.ecobin.ecobin.mapper.UserMapper;
import uz.ecobin.ecobin.model.EmailCode;
import uz.ecobin.ecobin.model.Role;
import uz.ecobin.ecobin.model.User;
import uz.ecobin.ecobin.repository.EmailCodeRepository;
import uz.ecobin.ecobin.repository.RoleRepository;
import uz.ecobin.ecobin.repository.UserRepository;
import uz.ecobin.ecobin.service.AuthenticationService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender emailSender;

    @Override
    public ResponseEntity<ApiResponse> register(RegisterRequest registerRequest, HttpServletRequest request) {
        Optional<User> byEmail = userRepository.findByEmail(registerRequest.getEmail());
        if (byEmail.isPresent()) {
            if (!(byEmail.get().isEnabled() || byEmail.get().isAccountNonLocked())) {

                userRepository.delete(byEmail.get());
            } else {
                throw new AlreadyExists("Email already exists");
            }
        }
        Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
        Role role = roleUser.orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
        var user = User
                .builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .locked(false)
                .enabled(false)
                .build();
        userRepository.save(user);

        Random random = new Random();
        int code = 10000 + random.nextInt(89999);
        boolean confirmEmail = sendEmail(user.getEmail(), "Confirm Email", "http://localhost:5174/confirm-email?code=" + code + "&user=" + user.getId());
        if (!confirmEmail) {
            userRepository.delete(user);
            throw new CommonException("Something is wrong. Please try again later.", HttpStatus.CONFLICT);
        }

        EmailCode emailCode = new EmailCode(null, user, Integer.toString(code), LocalDateTime.now());
        emailCodeRepository.save(emailCode);

        return ResponseEntity.ok(ApiResponse
                .builder()
                .isSuccess(true)
                .message("We are send code to your email. Please check your email. Expiration date : 10 minute")
                .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> confirmEmail(String code, Long user) {
        Optional<EmailCode> emailCode = emailCodeRepository.findByUserId(user);
        if (emailCode.isEmpty()) {
            throw new CommonException("Try again", HttpStatus.BAD_REQUEST);
        }
        EmailCode email = emailCode.get();
        if (!email.getCode().equals(code)) {
            throw new CommonException("Try again", HttpStatus.BAD_REQUEST);
        }

        User emailUser = email.getUser();
//        emailUser.setLocked(true);
//        emailUser.setEnabled(true);
        userRepository.save(emailUser);
        emailCodeRepository.delete(email);
        return ResponseEntity.ok(ApiResponse
                .builder()
                .message("Email confirmed")
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));

        emailCodeRepository.findByUserId(user.getId()).ifPresent(emailCodeRepository::delete);

        UUID uuid = UUID.randomUUID();

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(uuid.toString());
        emailCode.setUser(user);
        emailCode.setCreatedDateTime(LocalDateTime.now());

        emailCodeRepository.save(emailCode);

        String url = String.format("https://"+(user.getRole().getName().equals("ROLE_ADMIN") ? "supervisor" : "dashboard")+".naqdvision.uz/auth/reset-password?key=%s&email=%s", uuid, email);

        sendEmail(email, "Reset password", url);

        return ResponseEntity.ok(ApiResponse
                .builder()
                .status(201)
                .isSuccess(true)
                .message("We are send reset link to your email. Link expired 5 minutes")
                .build());
    }

    @Override
    public ResponseEntity<ApiResponse> resetPassword(ResetPasswordResponse passwordResponse) {
        User user = userRepository.findByEmail(passwordResponse.getEmail()).orElseThrow(() -> new NotFoundException("Email not found"));
        EmailCode emailCode = emailCodeRepository.findByUserId(user.getId()).orElseThrow(() -> new AuthenticationException("Something is wrong", HttpStatus.BAD_GATEWAY));
        if (!emailCode.getCode().equals(passwordResponse.getKey())) {
            throw new AuthenticationException("Something is wrong please try again later", HttpStatus.BAD_REQUEST);
        }
        if (emailCode.getCreatedDateTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Link expired. Please try again", HttpStatus.BAD_REQUEST);
        }
        if (!passwordResponse.getPassword().equals(passwordResponse.getNewPassword())){
            throw new AuthenticationException("Password is not equal", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(passwordResponse.getNewPassword()));
        userRepository.save(user);

        emailCodeRepository.delete(emailCode);

        return ResponseEntity.ok(ApiResponse.builder().status(201).isSuccess(true).message("Your password now changed").build());
    }

    @Override
    public ResponseEntity<ApiResponse> checkKey(String key, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));
        EmailCode emailCode = emailCodeRepository.findByUserId(user.getId()).orElseThrow(() -> new AuthenticationException("Something is wrong", HttpStatus.BAD_GATEWAY));
        if (!emailCode.getCode().equals(key)) {
            throw new AuthenticationException("Something is wrong please try again later", HttpStatus.BAD_REQUEST);
        }
        if (emailCode.getCreatedDateTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Link expired. Please try again", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(
                ApiResponse
                        .builder()
                        .isSuccess(true)
                        .status(200)
                        .build());
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest registerRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(registerRequest.getEmail())
                .orElseThrow();
        if (!(user.isLocked() || user.isEnabled())) {
            throw new AuthenticationException("You can enter the site after being approved by the admin", HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(null);
        user.setEmailCodes(null);
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthenticationResponse
                .builder()
                .token(jwtToken)
                .user(userMapper.toDto(user))
                .build());
    }

    private boolean sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setFrom("naqdvision@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (Exception e) {
            log.error("FAILED -> SEND EMAIL");
            log.error("FAILED -> SEND EMAIL");
            log.error("FAILED -> SEND EMAIL");
            return false;
        }
        return true;
    }
}
