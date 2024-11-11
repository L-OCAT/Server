package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.global.annotation.PublicApi;
import com.locat.api.global.auth.enums.AccessLevel;
import com.locat.api.global.auth.enums.KeyValidation;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/redirect")
public class OAuthRedirectDispatcher {

  @Value("${service.url.admin}")
  private String adminUrl;

  private final OAuth2Service oAuth2Service;

  @GetMapping("/kakao")
  public ResponseEntity<BaseResponse<Void>> doKakaoCallback(@RequestParam final String code) {
    return this.processOAuth(OAuth2ProviderType.KAKAO, code);
  }

  @PostMapping("/apple")
  public ResponseEntity<BaseResponse<Void>> doAppleCallback(@RequestParam final String code) {
    return this.processOAuth(OAuth2ProviderType.APPLE, code);
  }

  private ResponseEntity<BaseResponse<Void>> processOAuth(
      final OAuth2ProviderType providerType, final String code) {
    String oAuthId = this.oAuth2Service.authenticate(providerType, code);
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(adminUrl.concat("/login?oAuthId=").concat(oAuthId)))
        .build();
  }
}
