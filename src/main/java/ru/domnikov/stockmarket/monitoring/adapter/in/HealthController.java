
package ru.domnikov.stockmarket.monitoring.adapter.in;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.domnikov.stockmarket.monitoring.domain.HealthReport;
import ru.domnikov.stockmarket.monitoring.port.in.GetHealthUseCase;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final GetHealthUseCase useCase;

    public HealthController(GetHealthUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public HealthResponse health() {
        HealthReport report = useCase.getHealth();
        Map<String, HealthResponse.ComponentResponse> components = new LinkedHashMap<>();
        report.components().forEach((name, c) ->
                components.put(name, new HealthResponse.ComponentResponse(c.status().name(), c.details())));
        return new HealthResponse(report.status().name(), report.service(),
                report.version(), components);
    }
}