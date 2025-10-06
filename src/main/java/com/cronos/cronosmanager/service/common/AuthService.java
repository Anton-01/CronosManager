package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.response.LoginResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponseDto generateTokens(Authentication authentication);
}
