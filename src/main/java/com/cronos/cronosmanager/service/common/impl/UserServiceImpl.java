package com.cronos.cronosmanager.service.common.impl;


import com.cronos.cronosmanager.model.common.User;
import com.cronos.cronosmanager.repository.common.UserRepository;
import org.springframework.stereotype.Service;
import com.cronos.cronosmanager.service.common.UserService;
import lombok.RequiredArgsConstructor;

import static com.cronos.cronosmanager.utils.UserUtils.verifyCode;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public void resetLoginAttempts(String userId) {
        userRepository.resetLoginAttempts(userId);
    }

    @Override
    public void updateLoginAttempts(String email) {
        userRepository.updateLoginAttempts(email);
    }

    @Override
    public void setLastLogin(Long userId) {
        userRepository.setLastLogin(userId);
    }

    @Override
    public void addLoginDevice(Long userId, String deviceName, String client, String ipAddress) {
        userRepository.addLoginDevice(userId, deviceName, client, ipAddress);
    }

    @Override
    public boolean verifyQrCode(String userId, String code) {
        var user = userRepository.getUserByUuid(userId);
        return verifyCode(user.getQrCodeSecret(), code);
    }
}
