package com.blur.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.blur.auth.config.properties.AppProperties;
import com.blur.auth.config.properties.CorsProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    CorsProperties.class,
    AppProperties.class
})
public class BlurUserAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlurUserAuthApplication.class, args);
	}

}
