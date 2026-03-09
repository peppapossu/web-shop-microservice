package com.ks.authservice.service;

import com.ks.authservice.dto.registration.RegistrationRequest;

public interface RegistrationService {

    boolean register(RegistrationRequest request);
}
