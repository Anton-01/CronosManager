package com.cronos.cronosmanager.controller.common;

import com.cronos.cronosmanager.dto.common.request.LoginRequestDto;
import com.cronos.cronosmanager.dto.common.request.MfaVerificationRequestDto;
import com.cronos.cronosmanager.dto.common.request.RegisterRequestDto;
import com.cronos.cronosmanager.dto.common.response.LoginResponseDto;
import com.cronos.cronosmanager.exception.GlobalExceptionHandler;
import com.cronos.cronosmanager.model.common.User;
import com.cronos.cronosmanager.service.common.AuthService;
import com.cronos.cronosmanager.service.common.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cron/auth")
@RequiredArgsConstructor
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest) {
        try {
            User user = userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with UUID: " + user.getUserUuid());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        logger.info("Login request received. Request: {}", loginRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = (User) authentication.getPrincipal();

            logger.info("User logged in successfully with UUID: {}", user.getUserUuid());
            if (user.isMfa()) {
                return ResponseEntity.ok(
                        LoginResponseDto.builder()
                                .mfaRequired(true)
                                .message("MFA verification is required.")
                                .build()
                );
            }

            // Delegar la generación de tokens al servicio
            return ResponseEntity.ok(authService.generateTokens(authentication));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(LoginResponseDto.builder().message("Invalid credentials.").build());
        }
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<LoginResponseDto> verifyMfa(@Valid @RequestBody MfaVerificationRequestDto mfaRequest) {
        if (!userService.verifyQrCode(mfaRequest.getEmail(), mfaRequest.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponseDto.builder().message("Invalid MFA code.").build());
        }

        User user = userService.getUserByEmail(mfaRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // Delegar la generación de tokens al servicio
        return ResponseEntity.ok(authService.generateTokens(authentication));
    }
}
