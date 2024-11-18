package com.locat.api.domain.auth.service;

import com.locat.api.domain.auth.dto.internal.AdminLoginDto;
import com.locat.api.domain.auth.dto.response.AdminLoginResponse;
import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.security.exception.AuthenticationException;
import com.locat.api.global.security.exception.TokenException;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import org.springframework.security.access.AccessDeniedException;

public interface AuthService {

  /**
   * OAuth2 인증을 수행합니다.
   *
   * @param oAuthId OAuth ID
   * @return 인증된 사용자의 토큰 정보
   * @throws NoSuchEntityException 해당 OAuth ID를 가진 사용자가 없을 경우
   * @throws AccessDeniedException 사용자가 비활성화 상태일 경우
   * @see com.locat.api.domain.user.enums.StatusType 사용자 상태 타입
   */
  LocatTokenDto authenticate(String oAuthId);

  /**
   * 관리자 로그인 방식으로 사용자를 인증합니다.
   *
   * @param loginDto 관리자 로그인 정보
   * @return 관리자 로그인 응답(토큰, 비밀번호 만료 여부 등)
   * @throws NoSuchEntityException 해당 OAuth ID를 가진 사용자가 없을 경우
   * @throws AuthenticationException 사용자가 관리자 로그인을 요청할 권한이 없는 경우
   * @throws AccessDeniedException 사용자가 비활성화 상태일 경우
   * @see com.locat.api.domain.user.enums.StatusType 사용자 상태 타입
   */
  AdminLoginResponse authenticate(AdminLoginDto loginDto);

  /**
   * JWT 토큰을 재발급합니다.
   *
   * @param accessToken 유효하지만, 만료된 접근 토큰
   * @param refreshToken 갱신 토큰
   * @return 재발급된 토큰 정보
   * @throws TokenException 제시된 토큰이 유효하지 않은 경우(만료, 변조, 본인에게 발급되지 않은 토큰 등)
   */
  LocatTokenDto renew(String accessToken, String refreshToken);

  /**
   * 이메일 주소로 인증 코드를 전송합니다.
   *
   * @param email 인증 코드를 발송할 이메일 주소
   * @throws EmailAlreadySentException 이미 인증 코드를 발송한 이메일 주소인 경우
   */
  void sendVerificationEmail(String email);

  /**
   * 이메일 주소로 전송된 인증 코드를 검증합니다.
   *
   * @param email 이메일 주소
   * @param code 인증 코드
   * @throws AuthenticationException 인증 코드가 만료되었거나, 일치하지 않는 경우
   * @apiNote 인증 코드는 인증 성공 여부와 관계 없이, 1회 사용 후 폐기됩니다.
   */
  void verify(String email, String code);
}
