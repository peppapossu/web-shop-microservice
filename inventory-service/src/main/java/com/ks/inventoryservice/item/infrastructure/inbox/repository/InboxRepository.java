package com.ks.inventoryservice.item.infrastructure.inbox.repository;

import com.ks.inventoryservice.item.infrastructure.inbox.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, UUID> {

    @Modifying
    @Query(value = """
            INSERT INTO inbox (idempotent_key, status)
            VALUES (:id, 'IN_PROGRESS')
            ON CONFLICT (idempotent_key)
            DO UPDATE SET idempotent_key = inbox.idempotent_key
            RETURNING status, saved_result, xmax = 0 AS inserted
            """, nativeQuery = true)
    InboxProjection upsertAndReturn(@Param("id") UUID idempotentKey);

    @Modifying
    @Query(value = """
            UPDATE inbox
            SET status = 'SUCCEEDED',
                saved_result = :result
            WHERE idempotent_key = :id
              AND status = 'IN_PROGRESS'
            """,nativeQuery = true)
    int setStatusSucceeded(@Param("id") UUID idempotentKey,
                           @Param("result") byte[] createdResponse);

    @Modifying
    @Query(value = """
            UPDATE inbox
            SET status = 'FAILED'
            WHERE idempotent_key = :id AND status = 'IN_PROGRESS'
            """, nativeQuery = true)
    int setStatusFailed(@Param("id") UUID id);
}
