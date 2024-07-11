package com.locat.api.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    /**
     * Lecttuce 클라이언트 Connection Factory 설정
     */
    @Bean
    protected LettuceConnectionFactory redisConnectionFactory() {
        final String host = redisProperties.getHost();
        final Integer port = redisProperties.getPort();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(configuration);
    }

}
