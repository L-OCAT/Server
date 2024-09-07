package com.locat.api.domain.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 *
 * @param userId 사용자 ID (never {@code null})
 * @param deviceToken 사용자 디바이스 토큰
 * @param platform 사용자 플랫폼 정보 (ios, android)
 */
public record EndpointRegistrationRequest(
        @Positive Long userId,
        @NotBlank String deviceToken,
        @NotBlank String platform) {}
