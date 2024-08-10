package com.locat.api.domain.auth.controller;

import com.locat.api.domain.auth.dto.OAuth2AuthorizeRequest;
import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.core.BaseResponse;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final OAuth2Service oAuth2Service;

  @PostMapping
  public ResponseEntity<BaseResponse<LocatTokenDto>> authenticate(
      @RequestBody final OAuth2AuthorizeRequest request) {
    LocatTokenDto locatTokenDto =
        this.oAuth2Service.authenticate(request.providerType(), request.code());
    return ResponseEntity.ok((BaseResponse.of(locatTokenDto)));
  }
}
