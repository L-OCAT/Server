package com.locat.api.infra.client.http;

import com.locat.api.domain.auth.dto.internal.KakaoUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {

  /**
   * 사용자 정보 조회
   *
   * @param accessToken 사용자의 OAuth2 접근 토큰
   * @return 사용자 정보
   */
  @GetMapping("/v2/user/me")
  KakaoUserInfoDto getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

  /**
   * 사용자 연결 해제
   *
   * @param accessToken 사용자의 OAuth2 접근 토큰
   */
  @PostMapping("/v1/user/unlink")
  void withdrawal(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
