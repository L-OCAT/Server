package com.locat.api.domain.admin.controller;

import com.locat.api.domain.admin.dto.request.AdminPasswordResetRequest;
import com.locat.api.domain.admin.dto.request.AdminPromoteRequest;
import com.locat.api.domain.admin.service.AdminInternalService;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.global.annotation.AdminApi;
import com.locat.api.global.annotation.PublicApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
public class AdminController {

  private final AdminInternalService adminInternalService;

  @AdminApi(superAdminOnly = true)
  @PostMapping
  public ResponseEntity<BaseResponse<Void>> updateUserType(
      @RequestBody @Valid final AdminPromoteRequest request) {
    this.adminInternalService.updateUserType(request.id(), request.level());
    return ResponseEntity.noContent().build();
  }

  @PublicApi
  @PostMapping("/me/password")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @RequestBody @Valid final AdminPasswordResetRequest request) {
    this.adminInternalService.resetPassword(request.userId(), request.newPassword());
    return ResponseEntity.noContent().build();
  }
}
