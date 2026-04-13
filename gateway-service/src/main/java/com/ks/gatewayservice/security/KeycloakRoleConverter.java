package com.ks.gatewayservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLES ="roles";
    private static final String REALM_ACCESS = "realm_access";


    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt == null) {
            log.warn("JWT is null");
            return Collections.emptySet();
        }

        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);

        if (realmAccess == null) {
            log.debug("JWT does not contain '{}' claim", REALM_ACCESS);
            return Collections.emptySet();
        }

        Object rolesObj = realmAccess.get(ROLES);

        if (rolesObj == null) {
            log.debug("'{}' claim does not contain '{}'", REALM_ACCESS, ROLES);
            return Collections.emptySet();
        }

        if (!(rolesObj instanceof Collection<?> rolesCollection)) {
            log.warn("'{}' is not a collection: {}", ROLES, rolesObj.getClass().getName());
            return Collections.emptySet();
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Object roleObj : rolesCollection) {
            if (!(roleObj instanceof String role)) {
                log.warn("Skipping non-string role: {}", roleObj);
                continue;
            }

            if (role.isBlank()) {
                log.warn("Skipping blank role");
                continue;
            }

            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        }

        if (authorities.isEmpty()) {
            log.debug("No valid roles extracted from JWT");
        }

        return authorities;
    }
}
