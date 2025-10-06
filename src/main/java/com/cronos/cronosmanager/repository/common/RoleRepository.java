package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.enums.RoleEnum;
import com.cronos.cronosmanager.model.common.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleEnum name);
}
