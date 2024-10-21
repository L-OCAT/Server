package com.locat.api.domain.admin.controller;

import com.locat.api.domain.admin.dto.request.AdminPasswordResetRequest;
import com.locat.api.domain.admin.dto.request.AdminPromoteRequest;
import com.locat.api.domain.admin.service.AdminInternalService;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.global.annotation.AdminApi;
import com.locat.api.global.auth.LocatUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AdminApi
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

  private final AdminInternalService adminInternalService;

  @AdminApi(superAdminOnly = true)
  @PostMapping
  public ResponseEntity<BaseResponse<Void>> promote(
      @RequestBody @Valid final AdminPromoteRequest request) {
    this.adminInternalService.promote(request.id(), request.level());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid final AdminPasswordResetRequest request) {
    this.adminInternalService.resetPassword(userDetails.getId(), request.newPassword());
    return ResponseEntity.noContent().build();
  }
}
