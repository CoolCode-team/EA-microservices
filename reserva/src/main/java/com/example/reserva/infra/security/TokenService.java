package com.example.reserva.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.jwt.secret}")
    private String secret;

    private final String issuer = "EA";


    public String generateToken(Authentication authentication) {

        var authenticatedDetails = (UserAuthenticated) authentication.getPrincipal();
        var user = authenticatedDetails.getUser();

        Algorithm algorithm = Algorithm.HMAC256(secret);


        return JWT.create().withIssuer(issuer)
                .withSubject(user.getEmail())
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
