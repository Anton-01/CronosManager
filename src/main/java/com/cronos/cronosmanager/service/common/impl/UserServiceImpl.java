package com.cronos.cronosmanager.service.common.impl;


import com.cronos.cronosmanager.dto.common.request.RegisterRequestDto;
import com.cronos.cronosmanager.exception.common.ValidationException;
import com.cronos.cronosmanager.model.common.Credential;
import com.cronos.cronosmanager.model.common.Device;
import com.cronos.cronosmanager.model.common.Role;
import com.cronos.cronosmanager.model.common.User;
import com.cronos.cronosmanager.repository.common.DeviceRepository;
import com.cronos.cronosmanager.repository.common.RoleRepository;
import com.cronos.cronosmanager.repository.common.UserRepository;
import com.cronos.cronosmanager.utils.UserUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cronos.cronosmanager.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository deviceRepository;
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ValidationException("Email is already in use.");
        }

        Role userRole = roleRepository.findByName(registerRequest.getRole()).orElseThrow(() -> new ValidationException("Invalid role specified."));

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getEmail());
        user.setMemberId(UUID.randomUUID().toString());

        user.setRoles(Set.of(userRole));

        Credential credential = new Credential(passwordEncoder.encode(registerRequest.getPassword()), user);
        user.setCredential(credential);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void handleFailedLogin(String email) {
        User user = getUserByEmail(email);
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
            user.setAccountNonLocked(false);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void handleSuccessfulLogin(String email) {
        User user = getUserByEmail(email);
        user.setLoginAttempts(0);
        user.setLastLogin(ZonedDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyQrCode(String email, String code) {
        User user = getUserByEmail(email);
        return UserUtils.verifyCode(user.getQrCodeSecret(), code);
    }

    @Override
    @Transactional
    public void addLoginDevice(User user, String deviceName, String client, String ipAddress) {
        Device device = new Device();
        device.setUser(user);
        device.setDevice(deviceName);
        device.setClient(client);
        device.setIpAddress(ipAddress);
        deviceRepository.save(device);
    }
}
