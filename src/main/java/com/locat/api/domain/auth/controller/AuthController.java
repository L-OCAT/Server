package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.dto.EmailVerificationRequest;
import com.locat.api.domain.auth.dto.OAuth2AuthorizeRequest;
import com.locat.api.domain.auth.dto.TokenRenewRequest;
import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.core.BaseResponse;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final OAuth2Service oAuth2Service;

  @PostMapping
  public ResponseEntity<BaseResponse<String>> authenticate(
      @RequestBody final OAuth2AuthorizeRequest request) {
    String oauthId = this.oAuth2Service.authenticate(request.providerType(), request.code());
    return ResponseEntity.ok((BaseResponse.of(oauthId)));
  }

  @PostMapping("/renew")
  public ResponseEntity<BaseResponse<LocatTokenDto>> renewToken(
      @RequestBody final TokenRenewRequest request) {
    LocatTokenDto locatTokenDto =
        this.authService.renew(request.accessToken(), request.refreshToken());
    return ResponseEntity.ok((BaseResponse.of(locatTokenDto)));
  }

  @PostMapping("/email-verification")
  public ResponseEntity<Void> sendVerificationEmail(
      @RequestBody @Valid final EmailVerificationRequest request) {
    this.authService.sendVerificationEmail(request.email());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/email-verification/verify")
  public ResponseEntity<Void> verifyEmail(
      @RequestBody @Valid final EmailVerificationRequest request) {
    this.authService.verify(request.email(), request.code());
    return ResponseEntity.noContent().build();
  }
}
