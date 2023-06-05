package com.blur.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    
	Environment env;
	
    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }


    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            System.out.println(jwt);
            System.out.println(env.getProperty("jwt.secret").getBytes());
            if (!isJwtValid(jwt, exchange)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }


    
    private boolean isJwtValid(String jwt, ServerWebExchange exchange) {
        boolean returnValue = true;

        String userId = null;

        try {
            userId = Jwts.parserBuilder()
//            		.setSigningKey(env.getProperty("token.secret"))
            		.setSigningKey(Keys.hmacShaKeyFor(env.getProperty("jwt.secret").getBytes()))
//            		.setSigningKey(key)
                    .build()
            		.parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
            exchange.getRequest().mutate().header("userId", userId);
            
            System.out.println(env.getProperty("jwt.secret").getBytes());
        } catch (Exception ex) {
            returnValue = false;
        }

        if (userId == null || userId.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

}