package com.locat.api.infrastructure.redis.config;

import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 서버 설정 정보를 담는 클래스
 *
 * @param host 서버 호스트 주소
 * @param port 서버 포트
 * @param password 서버 비밀번호 <i>Local 환경 등에서는 nullable</i>
 */
@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(String host, Integer port, @Nullable String password) {}
