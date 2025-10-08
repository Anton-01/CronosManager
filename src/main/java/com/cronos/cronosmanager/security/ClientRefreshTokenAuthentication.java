package com.cronos.cronosmanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public class ClientRefreshTokenAuthentication extends OAuth2ClientAuthenticationToken {
    Logger logger = LoggerFactory.getLogger(ClientRefreshTokenAuthentication.class);
    public ClientRefreshTokenAuthentication(String clientId) {
        super(clientId, ClientAuthenticationMethod.NONE, null, null);
        logger.info(":: Client refresh token authentication has been created. With String");
    }

    public ClientRefreshTokenAuthentication(RegisteredClient registeredClient) {
        super(registeredClient, ClientAuthenticationMethod.NONE, null);
        logger.info(":: Client refresh token authentication has been created. With RegisteredClient");
    }
}
