package com.ks.authservice.security;

import com.ks.authservice.security.spring.UserDetailsImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl user)) {
            throw new AccessDeniedException("Unauthorized");
        }

        return user.getId();
    }
}