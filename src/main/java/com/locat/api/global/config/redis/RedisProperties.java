package com.locat.api.global.config.redis;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    /**
     * Redis 서버 호스트 주소
     */
    private String host;
    /**
     * Redis 서버 포트
     */
    private Integer port;
    /**
     * Redis 서버 비밀번호 <i>Local 환경 등에서는 nullable</i>
     */
    @Nullable private String password;
}
