
package ru.domnikov.stockmarket.heartbeat.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.domnikov.stockmarket.heartbeat.adapter.out.kafka.dto.HeartbeatEvent;
import ru.domnikov.stockmarket.heartbeat.adapter.out.kafka.dto.HeartbeatPayload;
import ru.domnikov.stockmarket.registration.application.RegistrationState;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HeartbeatProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;
    private final RegistrationState registrationState;

    @Value("${app.heartbeat.topic}")
    private String topic;

    @Value("${app.registration.secret-key}")
    private String secretKey;

    @Value("${app.registration.account-id}")
    private String accountId;

    @Value("${app.registration.group-id}")
    private String groupId;

    @Value("${app.registration.strategy-id}")
    private String strategyId;

    @Value("${app.registration.service-name}")
    private String serviceName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    // Создаётся один раз при запуске приложения
    private final String instanceId = UUID.randomUUID().toString();

    private final Instant startedAt = Instant.now();

    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void sendHeartbeat() {
        System.out.println("Heartbeat scheduler triggered");
        // До регистрации Heartbeat не отправляем
        if (!registrationState.isRegistered()) {
            return;
        }

        Instant now = Instant.now();

        HeartbeatPayload payload = new HeartbeatPayload(
                secretKey,
                accountId,
                groupId,
                strategyId,
                serviceName,
                instanceId,
                "UP",
                startedAt,
                now,
                0L,
                0L,
                null,
                Map.of("appVersion", appVersion)
        );

        HeartbeatEvent event = new HeartbeatEvent(
                UUID.randomUUID().toString(),
                "account.service.heartbeat",
                "1.0",
                now,
                null,
                null,
                serviceName,
                payload
        );
        try {
            String message = jsonMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, event.eventId(), message)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            System.err.println(
                                    "Heartbeat sending failed: "
                                            + exception.getMessage()
                            );
                        } else {
                            System.out.println("Heartbeat successfully sent");
                        }
                    });

        } catch (JacksonException exception) {
            System.err.println(
                    "Heartbeat serialization failed: "
                            + exception.getMessage()
            );
        }
    }
}
