package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.dto.request.EndpointRegisterRequest;
import com.locat.api.domain.user.enums.PlatformType;

public record EndpointRegisterDto(String deviceToken, PlatformType platformType) {

  public static EndpointRegisterDto fromRequest(EndpointRegisterRequest request) {
    return new EndpointRegisterDto(
        request.deviceToken(), PlatformType.fromValue(request.platform()));
  }
}
