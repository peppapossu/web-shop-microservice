package com.ks.inventoryservice.repository;

import com.ks.inventoryservice.domain.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {

    @Query(value = """
                       SELECT * FROM inbox
                       WHERE idempotent_key = :idempotentKey
            """, nativeQuery = true)
    Inbox findByIdempotentKey(@Param("idempotentKey") UUID idempotentKey);
}
