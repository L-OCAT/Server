package com.locat.api.domain.user.controller;

import com.locat.api.domain.admin.dto.response.AdminUserInfoResponse;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.dto.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.dto.UserRegisterDto;
import com.locat.api.domain.user.dto.request.UserInfoUpdateRequest;
import com.locat.api.domain.user.dto.request.UserRegisterRequest;
import com.locat.api.domain.user.dto.request.UserWithDrawalRequest;
import com.locat.api.domain.user.dto.response.UserInfoResponse;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserRegistrationService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.security.annotation.AdminApi;
import com.locat.api.global.security.annotation.PublicApi;
import com.locat.api.global.security.jwt.JwtProvider;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;
  private final JwtProvider jwtProvider;
  private final UserRegistrationService userRegistrationService;

  /** 회원가입(OAuth2 인증 이후에) */
  @PublicApi
  @PostMapping
  public ResponseEntity<BaseResponse<LocatTokenDto>> register(
      @RequestBody @Valid final UserRegisterRequest request) {
    User user = this.userRegistrationService.register(UserRegisterDto.fromRequest(request));
    LocatTokenDto locatTokenDto = this.jwtProvider.create(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of(locatTokenDto));
  }

  /** 내 정보 조회 */
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserInfoResponse>> me(
      @AuthenticationPrincipal LocatUserDetails userDetails) {
    final long userId = userDetails.getId();
    User user =
        this.userService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    UserInfoResponse userInfoResponse = UserInfoResponse.fromEntity(user);
    return ResponseEntity.ok(BaseResponse.of(userInfoResponse));
  }

  /** 내 정보(이메일 또는 닉네임) 수정 */
  @PatchMapping("/me")
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
  @PutMapping("/me/delete")
  public ResponseEntity<Void> deleteMe(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid final UserWithDrawalRequest request) {
    final long userId = userDetails.getId();
    this.userService.delete(userId, request.reason());
    return ResponseEntity.noContent().build();
  }

  @AdminApi
  @GetMapping
  public ResponseEntity<BaseResponse<Page<AdminUserInfoResponse>>> findAllByAdmin(
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String nickname,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      Pageable pageable) {
    AdminUserSearchCriteria criteria =
        AdminUserSearchCriteria.of(nickname, email, startDate, endDate);
    return ResponseEntity.ok(
        BaseResponse.of(
            this.userService.findAll(criteria, pageable).map(AdminUserInfoResponse::from)));
  }
}
