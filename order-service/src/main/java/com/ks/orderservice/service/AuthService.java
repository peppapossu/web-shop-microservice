package com.ks.orderservice.service;


import com.ks.orderservice.dto.auth.AuthRequest;
import com.ks.orderservice.entity.appUser.AppUser;

public interface AuthService {
    AppUser authenticate(AuthRequest request);
}
