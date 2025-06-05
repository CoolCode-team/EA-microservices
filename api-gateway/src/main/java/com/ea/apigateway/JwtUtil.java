package com.ea.apigateway;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtUtil {
    @Value("${api.security.jwt.secret}")
    private String secret;

    private final String issuer = "EA";

    public Map<String, String> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var decoded =  JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);


            var claims = decoded.getClaims();

            return Map.of(
                    "id", claims.get("id").asString(),
                    "email", claims.get("email").asString(),
                    "name", claims.get("name").asString(),
                    "role", claims.get("role").asString()
            );
        } catch (JWTVerificationException e) {

            return null;
        }
    }

}
