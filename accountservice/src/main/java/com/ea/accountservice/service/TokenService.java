package com.ea.accountservice.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.ea.accountservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.jwt.secret}")
    private String secret;

    private final String issuer = "EA";


    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(secret);


        return JWT.create().withIssuer(issuer)
                .withSubject(user.getId().toString())
                .withClaim("id", user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);

    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        var decoded =  JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);

        return decoded.getSubject();

    }


    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }

}
