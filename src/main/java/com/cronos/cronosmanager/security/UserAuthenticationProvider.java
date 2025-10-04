package com.cronos.cronosmanager.security;

import com.cronos.cronosmanager.service.common.UserService;
import com.cronos.cronosmanager.model.common.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider{

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            User user = userService.getUserByEmail((String) authentication.getPrincipal());
            validateUser.accept(user);
            if (encoder.matches((String) authentication.getCredentials(), user.getPassword())) {
                return authenticated(user, "[PROTECTED]", commaSeparatedStringToAuthorityList(user.getRole() + "," + user.getAuthorities()));
            } else {
                userService.updateLoginAttempts(user.getEmail());
                throw new BadCredentialsException("Incorrect email/password. Please try again.");
            }
        } catch (Exception exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
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
