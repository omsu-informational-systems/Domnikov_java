package com.Registration.UserRegistration.DTO;

import lombok.Data;

@Data
public class JwtAuthDto {
    private String token;
    private String refreshToken;
}
