package com.cronos.cronosmanager.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ClientRefreshTokenAuthenticationConverter implements AuthenticationConverter{
    Logger logger = LoggerFactory.getLogger(ClientRefreshTokenAuthenticationConverter.class);

    @Override
    public Authentication convert(HttpServletRequest request) {
        logger.info(":: Converting authentication header.");
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if(!grantType.equals(AuthorizationGrantType.REFRESH_TOKEN.getValue())) {
            return null;
        }

        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
        if(!StringUtils.hasText(clientId)) {
            return null;
        }
        return new ClientRefreshTokenAuthentication(clientId);
    }
}
