package com.locat.api.unit.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.*;

import com.locat.api.domain.admin.dto.response.AdminUserInfoResponse;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.controller.UserController;
import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.dto.request.UserInfoUpdateRequest;
import com.locat.api.domain.user.dto.request.UserRegisterRequest;
import com.locat.api.domain.user.dto.request.UserWithDrawalRequest;
import com.locat.api.domain.user.dto.response.UserInfoResponse;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.domain.user.service.UserRegistrationService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.security.jwt.JwtProvider;
import com.locat.api.global.security.jwt.dto.LocatTokenDto;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.security.userdetails.impl.LocatUserDetailsImpl;
import com.locat.api.global.utils.HashingUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  private static final long ID = 1L;
  private static final String EMAIL = "user@example.com";
  private static final String OAUTH_ID = "512351278326";
  private static final OAuth2ProviderType OAUTH_TYPE = OAuth2ProviderType.KAKAO;
  private static final String NICKNAME = "user123";
  private static final String PROFILE_IMAGE = "profile.jpg";
  private static final UserType USER_TYPE = UserType.ADMIN;
  private static final StatusType STATUS_TYPE = StatusType.ACTIVE;
  private User user;
  private LocatUserDetails userDetails;

  @InjectMocks private UserController controller;
  @Mock private UserService userService;
  @Mock private JwtProvider jwtProvider;
  @Mock private UserRegistrationService userRegistrationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.user =
        User.builder()
            .id(ID)
            .email(EMAIL)
            .emailHash(HashingUtils.hash(EMAIL))
            .oauthId(OAUTH_ID)
            .oauthType(OAUTH_TYPE)
            .nickname(NICKNAME)
            .profileImage(PROFILE_IMAGE)
            .userType(USER_TYPE)
            .statusType(STATUS_TYPE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .deletedAt(null)
            .build();
    this.userDetails = new LocatUserDetailsImpl(this.user);
  }

  @Test
  @DisplayName("회원가입에 성공해야 한다.")
  void shouldRegisterUser() {
    // Given
    UserRegisterRequest request = mock(UserRegisterRequest.class);
    given(this.userRegistrationService.register(any(UserRegisterDto.class))).willReturn(this.user);

    // When
    ResponseEntity<BaseResponse<LocatTokenDto>> result = this.controller.register(request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(CREATED);
  }

  @Test
  @DisplayName("내 정보 조회에 성공해야 한다.")
  void shouldGetMyInfo() {
    // Given
    given(this.userService.findById(ID)).willReturn(Optional.of(this.user));

    // When
    ResponseEntity<BaseResponse<UserInfoResponse>> result = this.controller.me(this.userDetails);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(OK);
    assertThat(result.getBody()).isNotNull().isExactlyInstanceOf(BaseResponse.class);
  }

  @Test
  @DisplayName("사용자 정보가 없다면, 예외를 던져야 한다.")
  void shouldThrowExceptionWhenUserNotFound() {
    // Given
    given(this.userService.findById(ID)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.controller.me(this.userDetails))
        .isExactlyInstanceOf(NoSuchEntityException.class);
  }

  @Test
  @DisplayName("내 정보 수정에 성공해야 한다.")
  void shouldUpdateMyInfo() {
    // Given
    UserInfoUpdateRequest request = mock(UserInfoUpdateRequest.class);
    given(this.userService.update(eq(ID), any())).willReturn(this.user);

    // When
    ResponseEntity<BaseResponse<UserInfoResponse>> result =
        this.controller.updateMe(this.userDetails, request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(OK);
    assertThat(result.getBody()).isNotNull().isExactlyInstanceOf(BaseResponse.class);
  }

  @Test
  @DisplayName("회원 탈퇴에 성공해야 한다.")
  void shouldDeleteUser() {
    // Given
    UserWithDrawalRequest request = new UserWithDrawalRequest("Reason to withdraw");

    // When
    ResponseEntity<Void> result = this.controller.deleteMe(this.userDetails, request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
  }

  @Test
  @DisplayName("[관리자] 사용자 목록 조회에 성공해야 한다.")
  void shouldGetUsers() {
    // Given
    String nickname = "user123";
    Pageable pageable = Pageable.unpaged();
    given(this.userService.findAll(any(), any())).willReturn(Page.empty());

    // When
    ResponseEntity<BaseResponse<Page<AdminUserInfoResponse>>> result =
        this.controller.findAllByAdmin(null, nickname, null, null, pageable);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(OK);
    assertThat(result.getBody()).isNotNull().isExactlyInstanceOf(BaseResponse.class);
  }
}
