package com.ks.orderservice.controller;

import com.ks.orderservice.dto.auth.AuthRequest;
import com.ks.orderservice.dto.auth.AuthResponse;
import com.ks.orderservice.dto.auth.RegRequest;
import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.entity.jwt.RefreshToken;
import com.ks.orderservice.security.jwt.JwtProperties;
import com.ks.orderservice.security.jwt.JwtService;
import com.ks.orderservice.service.AuthService;
import com.ks.orderservice.service.RefreshTokenService;
import com.ks.orderservice.service.RegService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final RegService regService;

    private static final String REFRESH_COOKIE_NAME = "refresh_token";
    private static final String REFRESH_COOKIE_PATH = "/api/v1/auth/refresh";

    @PostMapping("/reg")
    public ResponseEntity<?> reg(@RequestBody RegRequest request) {
        regService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request,
                                   HttpServletResponse response) {

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

        RefreshToken oldToken = refreshTokenService.validate(refreshToken);
        AppUser appUser = oldToken.getUser();

        refreshTokenService.revokeToken(oldToken);

        String access = jwtService.generateAccessToken(appUser);
        String newRefresh = jwtService.generateRefreshToken(appUser);

        refreshTokenService.saveRefreshToken(
                appUser,
                newRefresh,
                LocalDateTime.now().plus(jwtProperties.getRefreshExpiration(),ChronoUnit.MILLIS)
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
