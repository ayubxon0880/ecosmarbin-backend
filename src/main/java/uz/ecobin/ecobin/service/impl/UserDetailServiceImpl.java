package uz.ecobin.ecobin.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.ecobin.ecobin.exceptions.NotFoundException;
import uz.ecobin.ecobin.model.User;
import uz.ecobin.ecobin.repository.UserRepository;


@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Entering in loadUserByUsername Method...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        if(user == null){
            logger.error("Email not found: " + email);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        logger.info("User Authenticated Successfully..!!!");
        return user;
    }
}
