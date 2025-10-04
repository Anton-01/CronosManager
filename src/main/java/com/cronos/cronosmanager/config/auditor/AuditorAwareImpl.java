package com.cronos.cronosmanager.config.auditor;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.cronos.cronosmanager.constants.ConstantsSystem.USER_DEFAULT_SYSTEM;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(USER_DEFAULT_SYSTEM);
    }
}
