package ru.domnikov.stockmarket.monitoring.adapter.in;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

public record HealthResponse(String status, String service, String version,
                             Map<String, ComponentResponse> components) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ComponentResponse(String status, String details) {}
}