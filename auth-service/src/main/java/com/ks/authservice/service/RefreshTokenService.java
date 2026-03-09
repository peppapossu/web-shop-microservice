package com.ks.authservice.service;


import com.ks.authservice.entity.appUser.AppUser;
import com.ks.authservice.entity.jwt.RefreshToken;

import java.time.LocalDateTime;

public interface RefreshTokenService {

    void saveRefreshToken(AppUser user, String refreshToken, LocalDateTime expires);

    RefreshToken validate(String refreshToken);

    void revokeToken(RefreshToken token);

}
