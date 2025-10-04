package com.cronos.cronosmanager.utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;

public class RequestUtils {

    public static String getMessage(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return format("%s - Not found error", statusCode);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return format("%s - Internal server error", statusCode);
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return format("%s - Forbidden error", statusCode);
            }
        }
        return "An error occurred";
    }
}
