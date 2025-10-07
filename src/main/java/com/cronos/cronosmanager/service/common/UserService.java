package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.request.RegisterRequestDto;
import com.cronos.cronosmanager.model.common.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //User getUserByEmail(String email);
    //void resetLoginAttempts(String userUuid);
    //void updateLoginAttempts(String email);
    //boolean verifyQrCode(String userId, String code);
    //void setLastLogin(Long userId);
    //void addLoginDevice(Long userId, String deviceName, String client, String ipAddress);
    User registerUser(RegisterRequestDto registerRequest);
    User getUserByEmail(String email);
    void handleFailedLogin(String email);
    void handleSuccessfulLogin(String email);
    boolean verifyQrCode(String email, String code);
    //... inside UserService interface
    void addLoginDevice(User user, String device, String client, String ipAddress);
}
