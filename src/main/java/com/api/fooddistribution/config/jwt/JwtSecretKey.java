package com.api.fooddistribution.config.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

import static com.api.fooddistribution.global.GlobalVariables.secretJwt;

@Configuration
public class JwtSecretKey {
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretJwt.getBytes());
    }
}
