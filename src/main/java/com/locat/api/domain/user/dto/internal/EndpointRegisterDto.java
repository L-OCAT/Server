package com.locat.api.domain.user.dto.internal;

import com.locat.api.domain.user.dto.request.EndpointRegisterRequest;
import com.locat.api.domain.user.enums.PlatformType;

/**
 * Push Notification Endpoint 등록 DTO
 *
 * @param deviceToken 디바이스 토큰
 * @param platformType 플랫폼 타입
 */
public record EndpointRegisterDto(String deviceToken, PlatformType platformType) {

  public static EndpointRegisterDto fromRequest(EndpointRegisterRequest request) {
    return new EndpointRegisterDto(
        request.deviceToken(), PlatformType.fromValue(request.platform()));
  }
}
