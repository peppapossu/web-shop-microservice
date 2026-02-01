package com.ks.orderservice.service.impl;


import com.ks.orderservice.dto.auth.AuthRequest;
import com.ks.orderservice.entity.appUser.AppUser;
import com.ks.orderservice.repository.AppUserRepository;
import com.ks.orderservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;

    public AppUser authenticate(AuthRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid username or password");
        }

        return userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
