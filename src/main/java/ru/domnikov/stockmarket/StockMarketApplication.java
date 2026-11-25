
package ru.domnikov.stockmarket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.domnikov.stockmarket.registration.adapter.out.kafka.RegistrationProducer;

@SpringBootApplication
@EnableScheduling
public class StockMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMarketApplication.class, args);
    }

    @Bean
    public ApplicationRunner registrationRunner(
            RegistrationProducer registrationProducer
    ) {
        return args -> registrationProducer.sendRegistrationRequest();
    }
}
