package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.entity.OAuth2ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/redirect")
public class OAuthRedirectDispatcher {

  private final OAuth2Service oAuth2Service;

  @GetMapping("/{providerType}")
  public ResponseEntity<BaseResponse<String>> handleOAuth2Redirect(
      @PathVariable final String providerType, @RequestParam final String code) {
    OAuth2ProviderType provider = OAuth2ProviderType.valueOf(providerType.toUpperCase());
    String oauthId = this.oAuth2Service.authenticate(provider, code);
    return ResponseEntity.ok((BaseResponse.of(oauthId)));
  }
}
