package com.locat.api.unit.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.domain.user.entity.OAuth2ProviderType;
import com.locat.api.domain.user.entity.StatusType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class UserEntityTest {

  private static final String EMAIL = "user@example.com";
  private static final String OAUTH_ID = "512351278326";
  private static final OAuth2ProviderType OAUTH_TYPE = OAuth2ProviderType.APPLE;
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
            .oauthId(OAUTH_ID)
            .oauthType(OAUTH_TYPE)
            .nickname(NICKNAME)
            .profileImage(PROFILE_IMAGE)
            .userType(USER_TYPE)
            .statusType(STATUS_TYPE)
            .build();
  }

  @Test
  @DisplayName("User Entity Builder Test")
  void testUserBuilder() {
    // When & Then
    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(EMAIL);
    assertThat(user.getNickname()).isEqualTo(NICKNAME);
    assertThat(user.getProfileImage()).isEqualTo(PROFILE_IMAGE);
    assertThat(user.getUserType()).isEqualTo(USER_TYPE);
    assertThat(user.getStatusType()).isEqualTo(STATUS_TYPE);
    assertThat(user.getOauthId()).isEqualTo(OAUTH_ID);
    assertThat(user.getOauthType()).isEqualTo(OAUTH_TYPE);
    assertThat(user.getDeletedAt()).isNull();
  }

  @Test
  @DisplayName("User Entity Update Method Test")
  void testDelete() {
    // When
    user.delete();

    // Then
    assertThat(user.getDeletedAt()).isNotNull();
  }
}
