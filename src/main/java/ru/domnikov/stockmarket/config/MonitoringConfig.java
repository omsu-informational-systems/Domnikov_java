package ru.domnikov.stockmarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.domnikov.stockmarket.monitoring.application.GetHealthService;

@Configuration
public class MonitoringConfig {
    @Bean
    public GetHealthService getMonitoringConfig(@Value("${app.service-name}") String serviceName,
                                                @Value("${app.version}")  String version) {
        return new GetHealthService(serviceName, version);
    }
}
