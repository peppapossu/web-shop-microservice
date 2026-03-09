package com.ks.authservice.service.impl;

import com.ks.authservice.dto.registration.RegistrationRequest;
import com.ks.authservice.entity.appUser.AppUser;
import com.ks.authservice.entity.appUser.Role;
import com.ks.authservice.repository.AppUserRepository;
import com.ks.authservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegistrationRequest request) {
        AppUser newUser = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_USER))
                .build();

        appUserRepository.save(newUser);
        //userCreatedEventOutbox.save(userCreatedEvent);



        return true;
    }
}
