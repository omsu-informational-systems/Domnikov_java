package Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api")
@RestController

public class HealthController {

    public record HealthResponse(String status,
                                 String service,
                                 String version,
                                 Map<String,Object> components){

    }
    @GetMapping("/health")
    public HealthResponse health(){
        return new HealthResponse("UP","stock-market","1.0.0",Map.of());
    }

}
