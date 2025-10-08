package com.cronos.cronosmanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class CronosManagerApplication {

    @Value("${ui.app.url:http://localhost:3000/callback}")
    private String redirectUri;

    public static void main(String[] args) {
        SpringApplication.run(CronosManagerApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(RegisteredClientRepository registeredClientRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (registeredClientRepository.findByClientId("client") == null) {
                log.info("Registered client 'client' not found. Creating it...");
                try {
                    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                            .clientId("client")
                            .clientSecret(passwordEncoder.encode("secret"))
                            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                            .redirectUri(redirectUri)
                            .scope(OidcScopes.OPENID)
                            .scope(OidcScopes.PROFILE)
                            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                            .tokenSettings(TokenSettings.builder()
                                    .refreshTokenTimeToLive(Duration.ofDays(15))
                                    .accessTokenTimeToLive(Duration.ofHours(8))
                                    .build())
                            .build();

                    registeredClientRepository.save(registeredClient);
                    log.info("Registered client 'client' created successfully.");
                } catch (Exception e) {
                    log.error("An error occurred while trying to generate RegisteredClient. Cause {}", e.getMessage(), e);
                }
            } else {
                log.info("Registered client 'client' already exists. Skipping creation.");
            }
        };
    }
}
