package com.Registration.UserRegistration.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtProperties {
    private String header = "Authorization";
    private String prefix = "Bearer ";
    private String secretKey;
}
