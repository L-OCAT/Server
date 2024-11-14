package com.locat.api.global.security.jwt;

import com.locat.api.domain.user.entity.User;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtProvider {

  /**
   * {@link User} 정보를 기반으로 서버 토큰을 생성합니다.
   *
   * @param user 사용자 정보
   * @return 토큰 발급 응답 DTO
   * @apiNote 갱신 토큰은 캐시 저장소에 저장됩니다.
   */
  LocatTokenDto create(User user);

  /**
   * 액세스 토큰을 갱신합니다.
   *
   * @param oldAccessToken 이전 액세스 토큰
   * @param refreshToken 갱신 토큰
   * @return 갱신된 토큰 발급 응답 DTO
   */
  LocatTokenDto renew(String oldAccessToken, String refreshToken);

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
}
