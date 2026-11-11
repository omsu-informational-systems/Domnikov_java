package ru.domnikov.stockmarket.monitoring.application;

import ru.domnikov.stockmarket.monitoring.domain.ApplicationStatus;
import ru.domnikov.stockmarket.monitoring.domain.ComponentReport;
import ru.domnikov.stockmarket.monitoring.domain.ComponentStatus;
import ru.domnikov.stockmarket.monitoring.domain.HealthReport;
import ru.domnikov.stockmarket.monitoring.port.in.GetHealthUseCase;

import java.util.Map;

public class GetHealthService implements GetHealthUseCase {

    private final String serviceName;
    private final String version;

    public GetHealthService(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
    }

    @Override
    public HealthReport getHealth() {
        Map<String, ComponentReport> components = Map.of();
        boolean degraded = components.values().stream()
                .anyMatch(c -> c.status() == ComponentStatus.DOWN);
        ApplicationStatus status = degraded ? ApplicationStatus.DEGRADED : ApplicationStatus.UP;
        return new HealthReport(status, serviceName, version, components);
    }
}
