package ru.domnikov.stockmarket.registration.adapter.in.kafka.dto;

public record RegistrationResultPayload(
        String secretKey,
        String accountId,
        String groupId,
        String strategyId,
        String serviceName,
        String status,
        String reason
) {}