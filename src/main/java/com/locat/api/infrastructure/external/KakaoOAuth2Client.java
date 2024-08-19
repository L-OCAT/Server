package com.locat.api.infrastructure.external;

import com.locat.api.domain.auth.dto.KakaoOAuth2TokenDto;
import com.locat.api.infrastructure.external.config.KakaoOAuth2ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoOAuth2Client",
    url = "https://kauth.kakao.com",
    configuration = KakaoOAuth2ClientConfig.class)
public interface KakaoOAuth2Client {

  @PostMapping("/oauth/token")
  KakaoOAuth2TokenDto issueOrRenewToken(
      @RequestParam("grant_type") String grantType,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("code") String code);
}
