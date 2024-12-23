package com.locat.api.unit.user.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.locat.api.domain.user.service.impl.UserWithdrawalLogServiceImpl;
import com.locat.api.infra.persistence.user.UserWithdrawalLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserWithdrawlLogServiceTest {

  @InjectMocks private UserWithdrawalLogServiceImpl service;
  @Mock private UserWithdrawalLogRepository logRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원 탈퇴 로그 저장 테스트")
  void testSave() {
    // Given
    long id = 1L;
    String reason = "Reason to withdraw";

    // When & Then
    assertThatCode(() -> this.service.save(id, reason)).doesNotThrowAnyException();
  }
}
