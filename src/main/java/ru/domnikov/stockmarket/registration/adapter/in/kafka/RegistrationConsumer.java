
package ru.domnikov.stockmarket.registration.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import ru.domnikov.stockmarket.registration.application.RegistrationState;
import ru.domnikov.stockmarket.registration.adapter.in.kafka.dto.RegistrationResult;

@Component
@RequiredArgsConstructor
public class RegistrationConsumer {

    private final JsonMapper jsonMapper;
    private final RegistrationState registrationState;

    @Value("${app.registration.account-id}")
    private String accountId;

    @Value("${app.registration.group-id}")
    private String groupId;

    @Value("${app.registration.strategy-id}")
    private String strategyId;

    @Value("${app.registration.service-name}")
    private String serviceName;

    @KafkaListener(
            topics = "${app.registration.result-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(String message) {

        try {
            RegistrationResult result =
                    jsonMapper.readValue(message, RegistrationResult.class);

            // Проверяем тип события
            if (!"account.registration.result".equals(result.eventType())) {
                return;
            }

            // Проверяем, что ответ относится к нашему запросу
            if (!registrationState.matches(
                    result.correlationId(),
                    result.causationId()
            )) {
                return;
            }

            var payload = result.payload();

            // Проверяем идентификаторы нашего сервиса
            if (!accountId.equals(payload.accountId())
                    || !groupId.equals(payload.groupId())
                    || !strategyId.equals(payload.strategyId())
                    || !serviceName.equals(payload.serviceName())) {

                System.out.println("Registration result contains чужие identifiers");
                return;
            }

            // Обрабатываем результат регистрации
            switch (payload.status()) {

                case "REGISTERED" -> {
                    registrationState.markRegistered();

                    System.out.println("Registration successful!");
                    System.out.println("Account ID: " + payload.accountId());
                    System.out.println("Strategy ID: " + payload.strategyId());
                }

                case "REJECTED" -> {
                    System.err.println("Registration rejected!");
                    System.err.println("Reason: " + payload.reason());
                }

                default -> System.err.println(
                        "Unknown registration status: " + payload.status()
                );
            }

        } catch (Exception exception) {
            System.err.println(
                    "Failed to process registration result: "
                            + exception.getMessage()
            );
        }
    }
}
