package com.znyar.filter;

import com.znyar.auth.AuthClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final AuthClient authClient;

    public AuthenticationFilter(RouteValidator validator, AuthClient authClient) {
        super(Config.class);
        this.validator = validator;
        this.authClient = authClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new SecurityException("Missing required Authorization header");
                }
                String token = authHeader.substring(7);
                if (!authClient.isTokenValid(token)) {
                    throw new SecurityException("Invalid token");
                }
            }
            return chain.filter(exchange).onErrorResume(throwable -> {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                exchange.getResponse().getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
                String errorMessage = "{ \"message\": \"An internal server error occurred.\" }";
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes());
                return exchange.getResponse().writeWith(Mono.just(buffer));
            });
        });
    }

    public static class Config {

    }

}
