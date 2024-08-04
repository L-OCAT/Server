package com.locat.api.unit.entity;

import com.locat.api.domain.user.entity.StatusType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

  private static final String EMAIL = "user@example.com";
  private static final String NICKNAME = "user123";
  private static final String PROFILE_IMAGE = "profile.jpg";
  private static final UserType USER_TYPE = UserType.ADMIN;
  private static final StatusType STATUS_TYPE = StatusType.ACTIVE;
  @InjectMocks private User user;

  @BeforeEach
  void setUp() {
    try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
      // Given
      this.user =
          User.builder()
              .email(EMAIL)
              .nickname(NICKNAME)
              .profileImage(PROFILE_IMAGE)
              .userType(USER_TYPE)
              .statusType(STATUS_TYPE)
              .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @DisplayName("User Entity Builder Test")
  void testUserBuilder() {
    // When & Then
    assertAll(
        () -> assertNotNull(user),
        () -> assertEquals(EMAIL, user.getEmail()),
        () -> assertEquals(NICKNAME, user.getNickname()),
        () -> assertEquals(PROFILE_IMAGE, user.getProfileImage()),
        () -> assertEquals(USER_TYPE, user.getUserType()),
        () -> assertEquals(STATUS_TYPE, user.getStatusType()),
        () -> assertNull(user.getDeletedAt()));
  }

  @Test
  @DisplayName("User Entity Update Method Test")
  void testDelete() {
    // When
    user.delete();

    // Then
    assertNotNull(user.getDeletedAt());
  }
}
