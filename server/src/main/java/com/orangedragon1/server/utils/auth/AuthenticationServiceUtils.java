package com.orangedragon1.server.utils.auth;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationServiceUtils {

    public static String getCurrentToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (null != attributes) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            if (null != authHeader && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return null;
        }
        return null;
    }
}
