package ru.domnikov.stockmarket.heartbeat.adapter.out.kafka.dto;

import java.time.Instant;

public record HeartbeatEvent (
        String eventId,
        String eventType,
        String schemaVersion,
        Instant occurredAt,
        String correlationId,
        String causationId,
        String producer,
        HeartbeatPayload payload
)
{}
