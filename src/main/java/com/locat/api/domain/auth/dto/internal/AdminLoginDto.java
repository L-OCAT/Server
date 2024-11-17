package com.locat.api.domain.auth.dto.internal;

import com.locat.api.domain.auth.dto.request.AdminLoginRequest;

public record AdminLoginDto(String deviceId, String userId, String password) {

  public static AdminLoginDto fromRequest(AdminLoginRequest request) {
    return new AdminLoginDto(request.deviceId(), request.userId(), request.password());
  }
}
