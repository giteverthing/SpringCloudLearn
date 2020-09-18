package com.microservice.ruohan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    String host;

    @Value("${spring.redis.port}")
    int port;

    @Bean
    public RedisConnectionFactory getRedisConnectionFactory() {
        RedisConnectionFactory factory = new LettuceConnectionFactory(host, port);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> createRedisTemplate() {
        RedisSerializer serializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setConnectionFactory(getRedisConnectionFactory());

        return redisTemplate;
    }
}
