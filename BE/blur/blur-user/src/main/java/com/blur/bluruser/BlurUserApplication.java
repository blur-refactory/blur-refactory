package com.blur.bluruser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class BlurUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlurUserApplication.class, args);
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("http://localhost:3000");
		config.addAllowedOriginPattern("http://localhost:8081");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.addExposedHeader("X-username");
		config.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}