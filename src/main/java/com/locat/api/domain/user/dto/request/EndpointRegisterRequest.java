package com.locat.api.domain.user.dto.request;

import com.locat.api.domain.user.entity.PlatformType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * @param deviceToken 사용자 디바이스 토큰
 * @param platform 사용자 플랫폼 정보 ({@link PlatformType})
 */
public record EndpointRegisterRequest(
    @NotBlank String deviceToken,
    @Pattern(regexp = "android|ios", message = "Platform should be either 'ios' or 'android'")
        String platform) {}
