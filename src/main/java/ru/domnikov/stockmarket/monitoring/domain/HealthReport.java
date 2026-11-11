package ru.domnikov.stockmarket.monitoring.domain;

import java.util.Map;

public record HealthReport(ApplicationStatus status, String service, String version,
                           Map<String, ComponentReport> components) {
}
