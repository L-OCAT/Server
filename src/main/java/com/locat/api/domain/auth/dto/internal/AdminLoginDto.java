package com.locat.api.domain.auth.dto.internal;

import com.locat.api.domain.auth.dto.request.AdminLoginRequest;

/**
 * 관리자 로그인 DTO
 *
 * @param deviceId 디바이스 ID
 * @param userId 사용자 ID
 * @param password 비밀번호
 */
public record AdminLoginDto(String deviceId, String userId, String password) {

  public static AdminLoginDto fromRequest(AdminLoginRequest request) {
    return new AdminLoginDto(request.deviceId(), request.userId(), request.password());
  }
}
