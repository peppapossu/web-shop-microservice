package com.ks.orderservice.service.impl;

import com.ks.orderservice.dto.auth.RegRequest;
import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.entity.appUser.Role;
import com.ks.orderservice.repository.AppUserRepository;
import com.ks.orderservice.service.RegService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RegServiceImpl implements RegService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegRequest request) {
        AppUser newUser = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_USER))
                .build();

        appUserRepository.save(newUser);
        return true;
    }
}
