package com.cronos.cronosmanager.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserAuthenticationManager {

    private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager () {
        return new ProviderManager(userAuthenticationProvider);
    }
}
