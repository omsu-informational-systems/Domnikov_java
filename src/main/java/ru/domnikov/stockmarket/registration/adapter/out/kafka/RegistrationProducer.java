package ru.domnikov.stockmarket.registration.adapter.out.kafka;

import ru.domnikov.stockmarket.registration.application.RegistrationState;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.domnikov.stockmarket.registration.adapter.out.kafka.dto.RegistrationPayload;
import ru.domnikov.stockmarket.registration.adapter.out.kafka.dto.RegistrationRequest;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;
    private final RegistrationState registrationState;

    @Value("${app.registration.request-topic}")
    private String topic;

    @Value("${app.registration.secret-key}")
    private String secretKey;

    @Value("${app.registration.account-id}")
    private String accountId;

    @Value("${app.registration.account-name}")
    private String accountName;

    @Value("${app.registration.group-id}")
    private String groupId;

    @Value("${app.registration.strategy-id}")
    private String strategyId;

    @Value("${app.registration.strategy-name}")
    private String strategyName;

    @Value("${app.registration.service-name}")
    private String serviceName;

    @Value("${app.registration.description}")
    private String description;

    public void sendRegistrationRequest() {

        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();

        RegistrationPayload payload = new RegistrationPayload(
                secretKey,
                accountId,
                accountName,
                groupId,
                strategyId,
                strategyName,
                serviceName,
                description
        );

        RegistrationRequest request = new RegistrationRequest(
                eventId,
                "account.registration.request",
                "1.0",
                Instant.now(),
                correlationId,
                null,
                serviceName,
                payload
        );

        registrationState.save(eventId, correlationId);

        try {
            String message = jsonMapper.writeValueAsString(request);

            kafkaTemplate.send(topic, eventId, message)
                    .whenComplete((result, exception) -> {

                        if (exception != null) {
                            System.err.println(
                                    "Registration request sending failed: "
                                            + exception.getMessage()
                            );
                        } else {
                            System.out.println(
                                    "Registration request successfully sent"
                            );
                        }
                    });

        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "Cannot serialize registration request", exception
            );
        }
    }

}