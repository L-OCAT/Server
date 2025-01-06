package com.locat.api.unit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.security.userdetails.impl.LocatUserDetailsImpl;
import com.locat.api.global.utils.HashingUtils;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class LocatUserDetailsTest {

  @Test
  @DisplayName("LocatUserDetails 생성 & 메서드 테스트")
  void testLocatUserDetailsMethods() {
    // Given
    final User mockUser =
        User.builder()
            .id(1L)
            .nickname("LOCAT1523")
            .email("test@locat.kr")
            .emailHash(HashingUtils.hash("test@locat.kr"))
            .password("enCryptedPassword")
            .statusType(StatusType.ACTIVE)
            .userType(UserType.SUPER_ADMIN)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .deletedAt(null)
            .build();

    // When
    LocatUserDetails locatUserDetails = LocatUserDetailsImpl.from(mockUser);

    // Then
    assertAll(
        () -> assertThat(locatUserDetails.getId()).isEqualTo(mockUser.getId()),
        () -> assertThat(locatUserDetails.getUser()).isEqualTo(mockUser),
        () -> assertThat(locatUserDetails.getUsername()).isEqualTo(mockUser.getEmail()),
        () -> assertThat(locatUserDetails.getAuthorities()).hasSize(1),
        () -> assertThat(locatUserDetails.isSuperAdmin()).isTrue(),
        () -> assertThat(locatUserDetails.isAdmin()).isTrue(),
        () -> assertThat(locatUserDetails.isAccountNonExpired()).isTrue(),
        () -> assertThat(locatUserDetails.isAccountNonLocked()).isTrue(),
        () -> assertThat(locatUserDetails.isCredentialsNonExpired()).isTrue(),
        () -> assertThat(locatUserDetails.isEnabled()).isTrue());
  }
}
