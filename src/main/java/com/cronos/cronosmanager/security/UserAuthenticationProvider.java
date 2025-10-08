package com.cronos.cronosmanager.security;

import com.cronos.cronosmanager.service.common.UserService;
import com.cronos.cronosmanager.model.common.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider{
    Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info(":: Authenticating user by username - : {}", authentication.getName());
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            UserDetails userDetails = userService.loadUserByUsername(email);
            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException("Your account is locked due to too many failed login attempts.");
            }
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Your account is currently disabled.");
            }
            if (!userDetails.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("Your credentials have expired.");
            }

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                userService.handleSuccessfulLogin(email);
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            } else {
                userService.handleFailedLogin(email);
                throw new BadCredentialsException("Incorrect email or password.");
            }
        } catch (AuthenticationException e) {
            throw e; // Re-lanzar excepciones de seguridad como LockedException, etc.
        } catch (Exception e) {
            throw new BadCredentialsException("Incorrect email or password.");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationType);
    }

    private final Consumer<User> validateUser = user -> {
        if (!user.isAccountNonLocked() || user.getLoginAttempts() >= 5) {
            throw new LockedException(String.format(user.getLoginAttempts() > 0 ? "Account currently locked after %s failed login attempts." : "Account currently locked", user.getLoginAttempts()));
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Your account is currently disabled");
        }
        if (!user.isAccountNonExpired()) {
            throw new DisabledException("Your account has expired. Please contact administration");
        }
    };
}
