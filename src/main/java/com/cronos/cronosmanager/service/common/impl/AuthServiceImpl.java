package com.cronos.cronosmanager.service.common.impl;

import com.cronos.cronosmanager.dto.common.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
import com.cronos.cronosmanager.service.common.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final OAuth2AuthorizationService authorizationService;


    @Override
    public LoginResponseDto generateTokens(Authentication authentication) {
        logger.info(":: Generating tokens for authentication {}", authentication.getName());
        RegisteredClient registeredClient = registeredClientRepository.findByClientId("client");
        if (registeredClient == null) {
            throw new IllegalStateException("Client 'client' not found. Please ensure it is registered.");
        }

        Set<String> authorizedScopes = registeredClient.getScopes();
        logger.info(":: Authorized scopes: {}", authorizedScopes);
        logger.info(":: AuthorizationServerContextHolder.getContext(): {}", AuthorizationServerContextHolder.getContext());
        DefaultOAuth2TokenContext accessTokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .build();

        logger.info(":: Token context: {}", accessTokenContext);
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(accessTokenContext);
        if (generatedAccessToken == null) {
            throw new IllegalStateException("Access token could not be generated.");
        }
        OAuth2AccessToken accessToken = (OAuth2AccessToken) generatedAccessToken;

        // --- Generar Refresh Token ---
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN)) {
            DefaultOAuth2TokenContext refreshTokenContext = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(authentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(authorizedScopes)
                    .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                    .build();

            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(refreshTokenContext);
            if (generatedRefreshToken instanceof OAuth2RefreshToken) {
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            }
        }

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(authentication.getName())
                .authorizationGrantType(new org.springframework.security.oauth2.core.AuthorizationGrantType("password"))
                .authorizedScopes(authorizedScopes);

        authorizationBuilder.accessToken(accessToken);

        if (refreshToken != null) {
            authorizationBuilder.refreshToken(refreshToken);
        }

        authorizationService.save(authorizationBuilder.build());

        // --- Construir la Respuesta ---
        long expiresIn = ChronoUnit.SECONDS.between(Objects.requireNonNull(accessToken.getIssuedAt()), accessToken.getExpiresAt());
        return LoginResponseDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken != null ? refreshToken.getTokenValue() : null)
                .expiresIn(expiresIn)
                .mfaRequired(false)
                .message("Login successful.")
                .build();
    }

}
