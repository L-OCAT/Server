package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.locat.api.domain.auth.dto.internal.OAuth2UserInfo;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.helper.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

class UserEntityTest {

  private static final String EMAIL = "user@example.com";
  private static final String OAUTH_ID = "512351278326";
  private static final OAuth2ProviderType OAUTH_TYPE = OAuth2ProviderType.KAKAO;
  private static final String NICKNAME = "user123";
  private static final String PROFILE_IMAGE = "profile.jpg";
  private static final UserType USER_TYPE = UserType.ADMIN;
  private static final StatusType STATUS_TYPE = StatusType.ACTIVE;
  @InjectMocks private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // Given
    this.user =
        User.builder()
            .email(EMAIL)
            .emailHash(HashingUtils.hash(EMAIL))
            .oauthId(OAUTH_ID)
            .oauthType(OAUTH_TYPE)
            .nickname(NICKNAME)
            .profileImage(PROFILE_IMAGE)
            .userType(USER_TYPE)
            .statusType(STATUS_TYPE)
            .build();
  }

  @Test
  @DisplayName("Builder로 생성한 User 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testUserBuilder() {
    // When & Then
    assertThat(this.user).isNotNull();
    assertThat(this.user.getId()).isNull(); // auto incremented
    assertThat(this.user.getEmail()).isEqualTo(EMAIL);
    assertThat(this.user.getEmailHash()).isEqualTo(HashingUtils.hash(EMAIL));
    assertThat(this.user.getNickname()).isEqualTo(NICKNAME);
    assertThat(this.user.getProfileImage()).isEqualTo(PROFILE_IMAGE);
    assertThat(this.user.getUserType()).isEqualTo(USER_TYPE);
    assertThat(this.user.getStatusType()).isEqualTo(STATUS_TYPE);
    assertThat(this.user.getOauthId()).isEqualTo(OAUTH_ID);
    assertThat(this.user.getOauthType()).isEqualTo(OAUTH_TYPE);
    assertThat(this.user.getUserSettings()).isNullOrEmpty();
    assertThat(this.user.getTermsAgreements()).isNullOrEmpty();
    assertThat(this.user.getUserEndpoints()).isNullOrEmpty();
    assertThat(this.user.getDeletedAt()).isNull();
  }

  @Test
  @DisplayName("of() 메서드로 생성한 User 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testOf() {
    // Given
    OAuth2UserInfo mockUserInfoDto = TestDataFactory.create(OAUTH_ID, EMAIL);

    // When
    User createdUser = User.of(PROFILE_IMAGE, NICKNAME, "temp1234", mockUserInfoDto);

    // Then
    assertThat(createdUser).isNotNull();
    assertThat(createdUser.getEmail()).isEqualTo(EMAIL);
    assertThat(createdUser.getEmailHash()).isEqualTo(HashingUtils.hash(EMAIL));
    assertThat(createdUser.getNickname()).isEqualTo(NICKNAME);
    assertThat(createdUser.getProfileImage()).isNull();
    assertThat(createdUser.getUserType()).isEqualTo(UserType.USER);
    assertThat(createdUser.getStatusType()).isEqualTo(StatusType.ACTIVE);
    assertThat(createdUser.getOauthId()).isEqualTo(OAUTH_ID);
    assertThat(createdUser.getOauthType()).isEqualTo(OAUTH_TYPE);
    assertThat(createdUser.getUserSettings()).isNullOrEmpty();
    assertThat(createdUser.getTermsAgreements()).isNullOrEmpty();
    assertThat(createdUser.getUserEndpoints()).isNullOrEmpty();
    assertThat(createdUser.getDeletedAt()).isNull();
  }

  @Test
  @DisplayName("null이 아닌 값이 주어지면, User 객체의 필드 값을 업데이트해야 한다.")
  void testUpdate() {
    // Given
    String newEmail = "neew01@locat.kr";
    String newNickname = "newUser";

    // When
    User updatedUser = this.user.update(newEmail, newNickname);

    // Then
    assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
    assertThat(updatedUser.getEmailHash()).isEqualTo(HashingUtils.hash(newEmail));
    assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
  }

  @Test
  @DisplayName("null이 주어지면, User 객체의 필드 값을 업데이트하지 않아야 한다.")
  void testUpdateWithNull() {
    // Given & When
    User updatedUser = this.user.update(null, null);

    // Then
    assertThat(updatedUser.getEmail()).isEqualTo(EMAIL);
    assertThat(updatedUser.getEmailHash()).isEqualTo(HashingUtils.hash(EMAIL));
    assertThat(updatedUser.getNickname()).isEqualTo(NICKNAME);
  }

  @Test
  @DisplayName("User를 soft delete하면, 필드 값이 적절히 업데이트되어야 한다.")
  void testDelete() {
    // When
    this.user.delete();

    // Then
    assertThat(this.user.getStatusType()).isEqualTo(StatusType.INACTIVE);
    assertThat(this.user.getDeletedAt()).isNotNull();
  }

  @Test
  @DisplayName("User가 활성화 상태일 때 status 관련 메서드 테스트")
  void testIfUserActivated() {
    // When & Then
    assertThat(this.user.isActivated()).isTrue();
    assertThatCode(this.user::assertActivated).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("User가 비활성화 상태일 때 status 관련 메서드 테스트")
  void testIfUserNotActivated() {
    // Given
    this.user.updateStatus(StatusType.INACTIVE);

    // When & Then
    assertThat(this.user.isActivated()).isFalse();
    assertThatCode(this.user::assertActivated)
        .isExactlyInstanceOf(AccessDeniedException.class)
        .hasMessageStartingWith("Access Denied:");
  }
}
