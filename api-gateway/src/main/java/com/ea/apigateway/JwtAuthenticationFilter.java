package com.ea.apigateway;


import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;




@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var path = exchange.getRequest().getPath().value();
        if (path.startsWith("/auth") || path.startsWith("/admin/users")) {
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        var userMap = jwtUtil.validateToken(token);

        if (userMap == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userMap.get("id"))
                .header("X-User-Email", userMap.get("email"))
                .header("X-User-Name", userMap.get("name"))
                .header("X-User-Role", userMap.get("role"))
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());

    }
}