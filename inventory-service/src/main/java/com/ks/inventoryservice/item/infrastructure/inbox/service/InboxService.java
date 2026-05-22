package com.ks.inventoryservice.item.infrastructure.inbox.service;

import com.ks.inventoryservice.item.infrastructure.inbox.repository.InboxProjection;
import com.ks.inventoryservice.item.infrastructure.inbox.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InboxProjection insertIdempotentKey(UUID idempotentKey) {
        return inboxRepository.upsertAndReturn(idempotentKey);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int setStatusFailed(UUID idempotentKey) {
       return inboxRepository.setStatusFailed(idempotentKey);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int setStatusSucceeded(UUID idempotentKey, byte[] resultList) {
       return inboxRepository.setStatusSucceeded(idempotentKey,resultList);
    }


}
