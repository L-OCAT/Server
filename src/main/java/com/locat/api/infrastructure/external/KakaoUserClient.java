package com.locat.api.infrastructure.external;

import com.locat.api.domain.auth.dto.KakaoUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {

  @GetMapping("/v2/user/me")
  KakaoUserInfoDto getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

  @PostMapping("/v1/user/unlink")
  void withdrawal(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
