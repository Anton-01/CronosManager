package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //User getUserByUuid(String userId);
    //User getUserByEmail(String email);
    //void resetLoginAttempts(String userUuid);
    //void updateLoginAttempts(String email);
    //void setLastLogin(Long userId);
    //void addLoginDevice(Long userId, String device, String client, String ipAddress);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserUuid(String userUuid);
    boolean existsByEmail(String email);
}
