package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.dto.AdminLoginDto;
import com.locat.api.domain.auth.dto.request.AdminLoginRequest;
import com.locat.api.domain.auth.dto.request.EmailVerificationRequest;
import com.locat.api.domain.auth.dto.request.TokenIssueRequest;
import com.locat.api.domain.auth.dto.request.TokenRenewRequest;
import com.locat.api.domain.auth.dto.response.AdminLoginResponse;
import com.locat.api.domain.auth.service.AuthService;
import com.locat.api.domain.common.dto.BaseResponse;
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

  @PostMapping("/token")
  public ResponseEntity<BaseResponse<LocatTokenDto>> authenticate(
      @RequestBody @Valid final TokenIssueRequest request) {
    LocatTokenDto locatTokenDto = this.authService.authenticate(request.oAuthId());
    return ResponseEntity.ok((BaseResponse.of(locatTokenDto)));
  }

  @PostMapping("/admin/token")
  public ResponseEntity<BaseResponse<AdminLoginResponse>> authenticateAdmin(
      @RequestBody @Valid final AdminLoginRequest request) {
    AdminLoginResponse adminLoginResponse =
        this.authService.authenticate(AdminLoginDto.fromRequest(request));
    return ResponseEntity.ok((BaseResponse.of(adminLoginResponse)));
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
