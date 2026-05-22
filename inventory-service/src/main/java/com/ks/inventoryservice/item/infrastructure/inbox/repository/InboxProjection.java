package com.ks.inventoryservice.item.infrastructure.inbox.repository;

import com.ks.inventoryservice.item.domain.Status;


public interface InboxProjection {
    Status getStatus();
    byte[] getSavedResult();
    Boolean getInserted();
}
