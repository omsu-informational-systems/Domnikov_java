package ru.domnikov.stockmarket.heartbeat.adapter.out.kafka.dto;

import java.time.Instant;
import java.util.Map;

public record HeartbeatPayload (
        String secretKey,
        String accountId,
        String groupId,
        String strategyId,
        String serviceName,
        String instanceId,
        String status,
        Instant startedAt,
        Instant sentAt,
        long processedCandles,
        long publishedRecommendations,
        Long lastProcessedSequenceNumber,
        Map<String, String> metadata
){}
