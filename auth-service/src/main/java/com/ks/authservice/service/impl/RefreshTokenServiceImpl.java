package com.ks.authservice.service.impl;


import com.ks.authservice.entity.appUser.AppUser;
import com.ks.authservice.entity.jwt.RefreshToken;
import com.ks.authservice.repository.RefreshTokenRepository;
import com.ks.authservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    public void saveRefreshToken(AppUser user, String refreshToken, LocalDateTime expires) {
        String hash = tokenHash(refreshToken);

        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .tokenHash(hash)
                .expiresAt(expires)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(entity);
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String refreshToken) {
        String hash = tokenHash(refreshToken);

        RefreshToken token = repository.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        repository.save(token);
    }

    private String tokenHash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
