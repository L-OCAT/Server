package com.locat.api.unit.admin;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.admin.entity.AdminActivity;
import com.locat.api.domain.admin.service.impl.AdminAuditServiceImpl;
import com.locat.api.global.event.AdminAuditEvent;
import com.locat.api.infra.persistence.admin.AdminActivityRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class AdminAuditServiceTest {

  @InjectMocks private AdminAuditServiceImpl service;
  @Mock private AdminActivityRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("관리자 활동 기록에 성공한다.")
  void testDoRecord() {
    // Given
    AdminActivity mockActivity = mock(AdminActivity.class);

    // When & Then
    assertThatCode(() -> this.service.doRecord(mockActivity)).doesNotThrowAnyException();
    then(this.repository).should().save(mockActivity);
  }

  @Test
  @DisplayName("관리자 활동 이벤트가 발생하면, 관리자 활동을 기록한다.")
  void testHandleAdminAuditEvent() {
    // Given
    AdminAuditEvent mockEvent =
        AdminAuditEvent.builder()
            .email("test@test.com")
            .superAdminOnly(false)
            .method("POST")
            .requestUri("/v1/some/end-points")
            .httpStatus(200)
            .isSuccessful(true)
            .remoteAddress("127.0.0.1")
            .userAgent("Mozilla/5.0")
            .timestamp(LocalDateTime.now())
            .build();

    // When & Then
    assertThatCode(() -> this.service.handleAdminAuditEvent(mockEvent)).doesNotThrowAnyException();
    then(this.repository).should().save(any(AdminActivity.class));
  }
}
