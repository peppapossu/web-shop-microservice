package com.ks.notificationservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InboxRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean tryInsert(UUID eventId) {
        try {
            jdbcTemplate.update(
                "INSERT INTO processed_event(event_id, created_at) VALUES (?,?)",
                eventId, Timestamp.from(Instant.now())
            );
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }
}
