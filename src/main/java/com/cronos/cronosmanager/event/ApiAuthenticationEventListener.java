package com.cronos.cronosmanager.event;

import com.cronos.cronosmanager.model.common.User;
import com.cronos.cronosmanager.service.common.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.cronos.cronosmanager.utils.UserAgentUtils.*;

@Slf4j
@Component
@AllArgsConstructor
public class ApiAuthenticationEventListener {

    private final UserService userService;
    private final HttpServletRequest request;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            log.info("AuthenticationSuccess - User: {}", user.getEmail());
            userService.handleSuccessfulLogin(user.getEmail());
            userService.addLoginDevice(user, getDevice(request), getClient(request), getIpAddress(request));
        }
    }

    // This will not be fired because we need to explicitly trigger the authentication failure event inside the UserAuthenticationProvider.java. So I use an else condition instead in the UserAuthenticationProvider.java class.
    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String email) {
            log.info("AuthenticationFailure - Email: {}", email);
            userService.handleFailedLogin(email);
        }
    }
}
