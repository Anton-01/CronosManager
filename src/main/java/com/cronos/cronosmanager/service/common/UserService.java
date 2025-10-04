package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.model.common.User;

public interface UserService {
    User getUserByEmail(String email);
    void resetLoginAttempts(String userUuid);
    void updateLoginAttempts(String email);
    boolean verifyQrCode(String userId, String code);
    void setLastLogin(Long userId);
    void addLoginDevice(Long userId, String deviceName, String client, String ipAddress);
}
