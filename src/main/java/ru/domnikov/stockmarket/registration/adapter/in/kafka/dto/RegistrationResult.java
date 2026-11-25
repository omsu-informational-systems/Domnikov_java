package ru.domnikov.stockmarket.registration.adapter.in.kafka.dto;

import java.time.Instant;

public record RegistrationResult(
        String eventId,
        String eventType,
        String schemaVersion,
        Instant occurredAt,
        String correlationId,
        String causationId,
        String producer,
        RegistrationResultPayload payload
) {}