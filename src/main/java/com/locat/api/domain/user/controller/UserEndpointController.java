package com.locat.api.domain.user.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.dto.EndpointRegisterDto;
import com.locat.api.domain.user.dto.request.EndpointRegisterRequest;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/endpoints")
public class UserEndpointController {

  private final UserEndpointService userEndpointService;

  @PostMapping
  public ResponseEntity<BaseResponse<Void>> register(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid final EndpointRegisterRequest request) {
    this.userEndpointService.register(
        userDetails.getId(), EndpointRegisterDto.fromRequest(request));
    return ResponseEntity.ok(BaseResponse.empty());
  }
}
