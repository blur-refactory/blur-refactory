package com.blur.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableEurekaClient
public class BlurApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlurApigatewayApplication.class, args);
	}

}
