package ru.domnikov.stockmarket.monitoring.port.in;

import ru.domnikov.stockmarket.monitoring.domain.HealthReport;

public interface GetHealthUseCase {
    public HealthReport getHealth();
}
