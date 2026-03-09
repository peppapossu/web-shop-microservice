package com.ks.authservice.controller;

import com.ks.authservice.dto.auth.AuthRequest;
import com.ks.authservice.dto.auth.AuthResponse;
import com.ks.authservice.dto.registration.RegistrationRequest;
import com.ks.authservice.entity.appUser.AppUser;
import com.ks.authservice.entity.jwt.RefreshToken;
import com.ks.authservice.security.jwt.JwtProperties;
import com.ks.authservice.security.jwt.JwtService;
import com.ks.authservice.security.jwt.RsaKeyProperties;
import com.ks.authservice.service.AuthService;
import com.ks.authservice.service.RefreshTokenService;
import com.ks.authservice.service.RegistrationService;
import com.nimbusds.jose.jwk.JWKSet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final RegistrationService regService;
    private final JWKSet jwkSet;

    private static final String REFRESH_COOKIE_NAME = "refresh_token";
    private static final String REFRESH_COOKIE_PATH = "/api/v1/auth/refresh";

    @GetMapping("/jwks")
    public Map<String, Object> getJwks() {
        return this.jwkSet.toJSONObject();
    }

    @PostMapping("/reg")
    public ResponseEntity<?> reg(@RequestBody RegistrationRequest request) {
        log.debug("Received RegistrationRequest: {}", request);
        regService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request,
                                   HttpServletResponse response) {
        log.debug("Received AuthRequest: {}", request);

        AppUser user = authService.authenticate(request);
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        int expiration = (int) jwtProperties.getRefreshExpiration();

        refreshTokenService.saveRefreshToken(
                user,
                refresh,
                LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS)
        );

        response.addHeader(HttpHeaders.SET_COOKIE, getCookie(refresh, expiration).toString());
        return ResponseEntity.ok(new AuthResponse(access));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken, HttpServletResponse response) {

        log.debug("Received RefreshToken: {}", refreshToken);
        RefreshToken oldToken = refreshTokenService.validate(refreshToken);
        AppUser appUser = oldToken.getUser();

        refreshTokenService.revokeToken(oldToken);

        String access = jwtService.generateAccessToken(appUser);
        String newRefresh = jwtService.generateRefreshToken(appUser);

        refreshTokenService.saveRefreshToken(
                appUser,
                newRefresh,
                LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS)
        );

        int expiration = (int) jwtProperties.getRefreshExpiration();
        response.addHeader(HttpHeaders.SET_COOKIE, getCookie(newRefresh, expiration).toString());

        return ResponseEntity.ok(new AuthResponse(access));
    }

    private ResponseCookie getCookie(String refreshToken, int refreshExpiration) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(REFRESH_COOKIE_PATH)
                .maxAge(refreshExpiration / 1000)
                .build();
    }
}
