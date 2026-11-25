package ru.domnikov.stockmarket.registration.adapter.out.kafka.dto;

public record RegistrationPayload (
        String secretKey,
        String accountId,
        String accountName,
        String groupId,
        String strategyId,
        String strategyName,
        String serviceName,
        String description
){}


