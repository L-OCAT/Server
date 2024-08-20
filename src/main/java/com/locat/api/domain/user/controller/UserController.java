package com.locat.api.domain.user.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.user.dto.request.UserInfoUpdateRequest;
import com.locat.api.domain.user.dto.request.UserWithDrawalRequest;
import com.locat.api.domain.user.dto.response.UserInfoResponse;
import com.locat.api.domain.user.service.UserInfoUpdateDto;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.auth.LocatUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/me")
public class UserController {

  private final UserService userService;

  /** 내 정보 조회 */
  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<BaseResponse<UserInfoResponse>> me(
      @AuthenticationPrincipal LocatUserDetails userDetails) {
    final long userId = userDetails.getId();
    UserInfoResponse userInfoResponse =
        UserInfoResponse.fromEntity(this.userService.findById(userId));
    return ResponseEntity.ok(BaseResponse.of(userInfoResponse));
  }

  /** 내 정보(이메일 또는 닉네임) 수정 */
  @PatchMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<BaseResponse<UserInfoResponse>> updateMe(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid final UserInfoUpdateRequest request) {
    final long userId = userDetails.getId();
    UserInfoResponse userInfoResponse =
        UserInfoResponse.fromEntity(
            this.userService.update(userId, UserInfoUpdateDto.from(request)));
    return ResponseEntity.ok(BaseResponse.of(userInfoResponse));
  }

  /** 회원 탈퇴 */
  @PutMapping("/delete")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteMe(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid final UserWithDrawalRequest request) {
    final long userId = userDetails.getId();
    this.userService.delete(userId, request.reason());
    return ResponseEntity.noContent().build();
  }
}
