package com.locat.api.infrastructure.aws;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AWS 서비스에 접근하기 위한 정보를 담는 클래스
 *
 * @param accessKey 서비스 접근을 위한 Access Key
 * @param secretKey 서비스 접근을 위한 Secret Key
 * @param region 서비스가 위치한 리전
 */
@ConfigurationProperties(prefix = "service.aws")
public record AwsProperties(String accessKey, String secretKey, String region) {}
