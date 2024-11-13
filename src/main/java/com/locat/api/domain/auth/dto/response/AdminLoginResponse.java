package com.locat.api.domain.auth.dto.response;

import com.locat.api.global.auth.jwt.LocatTokenDto;

/**
 * 관리자 로그인 응답
 *
 * @param isPasswordExpired 비밀번호 만료 여부
 * @param needMfa MFA(2차 인증) 필요 여부
 * @param token 발급된 토큰 정보
 */
public record AdminLoginResponse(Boolean isPasswordExpired, Boolean needMfa, LocatTokenDto token) {

  public static AdminLoginResponse of(
      Boolean isPasswordExpired, Boolean needMfa, LocatTokenDto token) {
    return new AdminLoginResponse(isPasswordExpired, needMfa, token);
  }
}
