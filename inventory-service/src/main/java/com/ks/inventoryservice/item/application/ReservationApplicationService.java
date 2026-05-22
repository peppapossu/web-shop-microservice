package com.ks.inventoryservice.item.application;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ks.inventoryservice.exception.BusinessException;
import com.ks.inventoryservice.item.infrastructure.inbox.repository.InboxProjection;
import com.ks.inventoryservice.item.infrastructure.inbox.service.InboxService;
import com.ks.inventoryservice.item.service.ItemService;
import com.ks.items.v1.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationApplicationService {

    private final ItemService itemService;
    private final InboxService inboxService;


    @Transactional
    public ReserveResponse reservationRequestHandler(ReserveRequest request) throws InvalidProtocolBufferException {

        UUID idempotentKey = UUID.fromString(request.getId());
        InboxProjection insertResult = inboxService.insertIdempotentKey(idempotentKey); //Propagation.REQUIRES_NEW

        ReserveResponse.Builder messageBuilder = ReserveResponse.newBuilder();

        if (!insertResult.getInserted()) {

            return switch (insertResult.getStatus()) {
                case SUCCEEDED -> messageBuilder
                        .setStatus(ReserveStatus.SUCCESS)
                        .setReserveItemResponseList(ReserveItemResponseList.parseFrom(insertResult.getSavedResult())).build();
                case IN_PROGRESS -> messageBuilder.setStatus(ReserveStatus.RETRY).build();
                case FAILED -> messageBuilder.setStatus(ReserveStatus.FAILED).build();
            };
        }

        try {
            ReserveItemResponseList itemResponseList = ReserveItemResponseList.newBuilder()
                    .addAllItemResponse(itemService.checkAndReserve(request))
                    .build();

            inboxService.setStatusSucceeded(idempotentKey, itemResponseList.toByteArray()); //Propagation.REQUIRES_NEW

            return messageBuilder.setStatus(ReserveStatus.SUCCESS)
                    .setReserveItemResponseList(itemResponseList).build();

        } catch (BusinessException ex) {
            log.error("BusinessException occurred", ex);
            inboxService.setStatusFailed(idempotentKey); //Propagation.REQUIRES_NEW
            return messageBuilder.setStatus(ReserveStatus.FAILED).build();
        }
    }
}
