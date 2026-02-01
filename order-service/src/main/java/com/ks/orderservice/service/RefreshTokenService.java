package com.ks.orderservice.service;


import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.entity.jwt.RefreshToken;

import java.time.LocalDateTime;

public interface RefreshTokenService {

    void saveRefreshToken(AppUser user, String refreshToken, LocalDateTime expires);

    RefreshToken validate(String refreshToken);

    void revokeToken(RefreshToken token);

}
