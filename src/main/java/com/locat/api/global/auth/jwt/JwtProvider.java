package com.locat.api.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtProvider {

  /**
   * 토큰 생성
   *
   * @param userEmail 사용자 이메일
   * @return 토큰 발급 응답 DTO
   */
  LocatTokenDto create(String userEmail);

  /**
   * {@link HttpServletRequest}의 Authorization Header에서 토큰을 추출합니다.
   *
   * @param request HTTP 요청
   * @return 토큰 | null (Bearer 형식이 아니거나, 토큰이 없는 경우)
   */
  String resolve(HttpServletRequest request);

  /**
   * 토큰을 파싱하여 사용자 정보를 반환합니다.
   *
   * @param token 액세스 토큰
   * @return 사용자 정보
   * @throws ExpiredJwtException 만료된 토큰을 사용할 경우
   */
  Claims parse(String token);

  /**
   * 토큰의 유효성을 검증하고, 상황에 따라 예외를 던집니다.
   *
   * @throws SecurityException 유효하지 않은 토큰을 사용할 경우
   * @throws UnsupportedJwtException 지원되지 않는 토큰을 사용할 경우
   * @throws ExpiredJwtException 만료된 토큰을 사용할 경우
   */
  void validate(String token);
}
