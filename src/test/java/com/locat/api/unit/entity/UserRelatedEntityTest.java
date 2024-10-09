package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.user.entity.UserWithdrawalLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRelatedEntityTest {

  @Test
  @DisplayName("UserWithdrawalLog Entity 생성 테스트")
  void testUserWithdrawalLog() {
    // Given
    final Long userId = 1L;
    final String reason = "탈퇴 사유";

    // When
    final UserWithdrawalLog userWithdrawalLog = UserWithdrawalLog.of(userId, reason);

    // Then
    assertThat(userWithdrawalLog).isNotNull();
    assertThat(userWithdrawalLog.getId()).isNull(); // auto incremented
    assertThat(userWithdrawalLog.getUserId()).isEqualTo(1L);
    assertThat(userWithdrawalLog.getReason()).isEqualTo("탈퇴 사유");
  }
}
