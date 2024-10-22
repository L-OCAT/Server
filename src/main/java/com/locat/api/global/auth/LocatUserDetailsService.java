package com.locat.api.global.auth;

import com.locat.api.global.exception.NoSuchEntityException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LocatUserDetailsService extends UserDetailsService {

  /**
   * 사용자 인증 정보로 인증 객체 {@link Authentication}을 생성합니다.
   *
   * @param userDetails 사용자 정보
   * @return 인증 객체 {@link Authentication}
   * @throws NoSuchEntityException 해당 ID를 가지는 사용자가 없을 경우
   */
  Authentication createAuthentication(UserDetails userDetails);

  Authentication createAuthentication(Claims claims);

  /**
   * 인증 객체로부터 (단일) 권한 정보를 추출합니다.
   *
   * @param authentication 인증 객체
   * @return 권한 정보
   */
  String extractAuthority(Authentication authentication);
}
