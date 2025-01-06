package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.admin.entity.AdminActivity;
import com.locat.api.global.event.AdminAuditEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class AdminActivityTest {

  @Test
  @DisplayName("AdminAuditEvent로 적절하게 AdminActivity를 생성한다.")
  void testFrom() {
    // Given
    var auditEvent =
        AdminAuditEvent.builder()
            .email("test@locat.kr")
            .superAdminOnly(true)
            .method("GET")
            .requestUri("/admin/activities")
            .httpStatus(200)
            .isSuccessful(true)
            .remoteAddress("127.0.0.1")
            .userAgent("Mozilla/5.0")
            .build();

    // When
    var adminActivity = AdminActivity.from(auditEvent);

    // Then
    assertThat(adminActivity)
        .isNotNull()
        .satisfies(
            activity -> {
              assertThat(activity.getEmail()).isEqualTo(auditEvent.email());
              assertThat(activity.getSuperAdminOnly()).isEqualTo(auditEvent.superAdminOnly());
              assertThat(activity.getHttpMethod()).isEqualTo(auditEvent.method());
              assertThat(activity.getUri()).isEqualTo(auditEvent.requestUri());
              assertThat(activity.getHttpStatus()).isEqualTo(auditEvent.httpStatus());
              assertThat(activity.getIsSuccessful()).isEqualTo(auditEvent.isSuccessful());
              assertThat(activity.getRemoteAddress()).isEqualTo(auditEvent.remoteAddress());
              assertThat(activity.getUserAgent()).isEqualTo(auditEvent.userAgent());
            });
  }
}
