package com.ks.orderservice.common.id;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidV7Generator implements IdGenerator {

    public UUID next() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
