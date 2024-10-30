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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/redirect")
public class OAuthRedirectDispatcher {

  @Value("${service.url.admin}")
  private String adminUrl;

  private final OAuth2Service oAuth2Service;

  @PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
  @GetMapping("/{providerType}")
  public ResponseEntity<BaseResponse<Void>> handleOAuth2Redirect(
      @PathVariable final String providerType, @RequestParam final String code) {
    OAuth2ProviderType provider = OAuth2ProviderType.valueOf(providerType.toUpperCase());
    String oauthId = this.oAuth2Service.authenticate(provider, code);
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(this.adminUrl.concat("/?oAuthId=").concat(oauthId)))
        .build();
  }
}
