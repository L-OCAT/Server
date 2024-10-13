package com.locat.api.domain.auth.dto.response;

import com.locat.api.global.auth.jwt.LocatTokenDto;

public record AdminLoginResponse(Boolean isPasswordExpired, Boolean needMfa, LocatTokenDto token) {

  public static AdminLoginResponse of(
      Boolean isPasswordExpired, Boolean needMfa, LocatTokenDto token) {
    return new AdminLoginResponse(isPasswordExpired, needMfa, token);
  }
}
