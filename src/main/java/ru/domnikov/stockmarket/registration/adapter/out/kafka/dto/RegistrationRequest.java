package ru.domnikov.stockmarket.registration.adapter.out.kafka.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "eventId",
        "eventType",
        "schemaVersion",
        "occurredAt",
        "correlationId",
        "causationId",
        "producer",
        "payload"
})
public record RegistrationRequest (
        String eventId,
        String eventType,
        String schemaVersion,
        Instant occurredAt,
        String correlationId,
        String causationId,
        String producer,
        RegistrationPayload payload
){}
