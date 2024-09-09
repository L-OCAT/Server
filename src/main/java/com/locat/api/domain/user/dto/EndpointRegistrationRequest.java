package com.locat.api.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @param deviceToken 사용자 디바이스 토큰
 * @param platform 사용자 플랫폼 정보 (ios, android)
 */
public record EndpointRegistrationRequest(
        @NotBlank String deviceToken,
        @NotBlank String platform) {}
