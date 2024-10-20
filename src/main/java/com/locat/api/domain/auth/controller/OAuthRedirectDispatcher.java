package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/redirect")
public class OAuthRedirectDispatcher {

  private final OAuth2Service oAuth2Service;

  @GetMapping("/{providerType}")
  public ResponseEntity<BaseResponse<Void>> handleOAuth2Redirect(
      @PathVariable final String providerType, @RequestParam final String code) {
    OAuth2ProviderType provider = OAuth2ProviderType.valueOf(providerType.toUpperCase());
    String oauthId = this.oAuth2Service.authenticate(provider, code);
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create("http://localhost:3000/?oAuthId=" + oauthId))
        .build();
  }
}
