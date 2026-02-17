package com.ks.orderservice.entity.id;

import java.util.UUID;

public interface IdGenerator {

    UUID next();
}
