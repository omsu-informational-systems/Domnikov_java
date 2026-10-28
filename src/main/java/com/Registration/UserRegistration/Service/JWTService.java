package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Configuration.JwtProperties;
import com.Registration.UserRegistration.DTO.JwtAuthDto;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTService {

    private static final Logger LOGGER = LogManager.getLogger(JWTService.class);
    private final JwtProperties jwtProperties;


    public JwtAuthDto generateAuthJwtToken(String username) {
        JwtAuthDto jwtDto = new JwtAuthDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshJwtToken(username));
        return jwtDto;
    }

    public JwtAuthDto refreshBaseToken(String username, String refreshToken){
        JwtAuthDto jwtDto = new JwtAuthDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromJwtToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch(ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        } catch(UnsupportedJwtException unEx){
            LOGGER.error("Unsupported JwtException", unEx);
        } catch(MalformedJwtException mexEx){
            LOGGER.error("Malformed JwtException", mexEx);
        }catch(SecurityException se){
            LOGGER.error("Security Exception", se);
        }catch(Exception e){
            LOGGER.error("Invalid Token", e);
        }
        return false;
    }

    //использую JWS токен, а не JWE так как не храню важные данные
    public String generateJwtToken(String username) {
        Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }
    public String generateRefreshJwtToken(String username) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }
    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
