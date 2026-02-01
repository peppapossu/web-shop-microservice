package com.ks.orderservice.service;

import com.ks.orderservice.dto.auth.RegRequest;

public interface RegService {

    boolean register(RegRequest request);
}
