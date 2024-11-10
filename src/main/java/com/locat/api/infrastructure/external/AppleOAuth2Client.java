package com.locat.api.infrastructure.external;

import com.locat.api.domain.auth.dto.response.ApplePublicKeysResponse;
import com.locat.api.domain.auth.dto.token.AppleOAuth2TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appleOAuth2Client", url = "https://appleid.apple.com/auth")
public interface AppleOAuth2Client {

  @GetMapping("/keys")
  ApplePublicKeysResponse getPublicKeys();

  /**
   * Apple OAuth2 토큰 발급 또는 갱신
   *
   * @param clientId Client ID(App ID 또는 Services ID)
   * @param clientSecret client에 의해 생성된 JWT 기반 client secret
   * @param code 인가 요청으로부터 발급된 인가 코드
   * @param grantType 인가 코드 교환 요청시 {@code authorization_code}, 리프레시 토큰 갱신 요청시 {@code refresh_token}
   * @param redirectUri 인가 요청시 사용된 리다이렉트 URI
   * @param refreshToken 리프레시 토큰 갱신 요청시 사용되는 리프레시 토큰
   * @return Apple OAuth2 토큰
   */
  @PostMapping("/token")
  AppleOAuth2TokenDto issueOrRenewToken(
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("code") String code,
      @RequestParam("grant_type") String grantType,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("refresh_token") String refreshToken);

  @PostMapping("/revoke")
  void revokeToken(
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("token") String token,
      @RequestParam("token_type_hint") String tokenTypeHint);
}
