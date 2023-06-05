package com.blur.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")
    private String redisHostName;
    @Value("${spring.redis.port}")
    private int redisPort;
    
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
//    	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    	redisTemplate.setConnectionFactory(connectionFactory);
//    	redisTemplate.setKeySerializer(new StringRedisSerializer());
//    	redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//    	return redisTemplate;
//    }
    
    @Bean
    public RedisTemplate<String , String> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String ,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
      RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
      redisStandaloneConfiguration.setHostName(redisHostName);
      redisStandaloneConfiguration.setPort(redisPort);
      return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
