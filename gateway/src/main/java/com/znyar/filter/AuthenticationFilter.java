package com.znyar.filter;

import com.znyar.auth.AuthClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.*;

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
                if (!authClient.isTokenValid(authHeader)) {
                    throw new SecurityException("Invalid token");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

}
