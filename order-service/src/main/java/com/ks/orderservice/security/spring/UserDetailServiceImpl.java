package com.ks.orderservice.security.spring;

import com.ks.orderservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(name)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(name));
    }
    public UserDetailsImpl loadUserById(Long id) throws UsernameNotFoundException {
        return appUserRepository.findById(id)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("id:" + id));
    }
}
