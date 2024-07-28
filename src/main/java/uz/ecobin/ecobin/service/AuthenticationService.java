package uz.ecobin.ecobin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import uz.ecobin.ecobin.dto.request.AuthenticationRequest;
import uz.ecobin.ecobin.dto.request.RegisterRequest;
import uz.ecobin.ecobin.dto.response.ApiResponse;
import uz.ecobin.ecobin.dto.response.AuthenticationResponse;
import uz.ecobin.ecobin.dto.response.ResetPasswordResponse;

public interface AuthenticationService {
    ResponseEntity<ApiResponse> register(RegisterRequest registerRequest, HttpServletRequest httpServletRequest);
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest registerRequest);
    ResponseEntity<?> confirmEmail(String code,Long user);
    ResponseEntity<ApiResponse> forgotPassword(String email);
    ResponseEntity<ApiResponse> resetPassword(ResetPasswordResponse passwordResponse);
    ResponseEntity<ApiResponse> checkKey(String key,String email);
}
