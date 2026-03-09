package com.ks.authservice.service;


import com.ks.authservice.dto.auth.AuthRequest;
import com.ks.authservice.entity.appUser.AppUser;

public interface AuthService {
    AppUser authenticate(AuthRequest request);
}
