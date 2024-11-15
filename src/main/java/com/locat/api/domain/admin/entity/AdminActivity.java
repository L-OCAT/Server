package com.locat.api.domain.admin.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.global.event.AdminAuditEvent;
import com.locat.api.global.persistence.converter.StringColumnEncryptionConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "admin_activity")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminActivity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 100)
  @NotNull @Column(name = "email", nullable = false, length = 100)
  @Convert(converter = StringColumnEncryptionConverter.class)
  protected String email;

  @Column(name = "super_admin_only", nullable = false)
  private Boolean superAdminOnly;

  @Column(name = "http_method", nullable = false, length = 10)
  private String httpMethod;

  @Column(name = "uri", nullable = false)
  private String uri;

  @Column(name = "http_status", nullable = false)
  private Integer httpStatus;

  @Column(name = "is_successful", nullable = false)
  private Boolean isSuccessful;

  @Column(name = "remote_address", nullable = false, length = 15)
  private String remoteAddress;

  @Column(name = "user_agent", nullable = false)
  private String userAgent;

  public static AdminActivity from(AdminAuditEvent auditEvent) {
    return AdminActivity.builder()
        .email(auditEvent.email())
        .superAdminOnly(auditEvent.superAdminOnly())
        .httpMethod(auditEvent.method())
        .uri(auditEvent.requestUri())
        .httpStatus(auditEvent.httpStatus())
        .isSuccessful(auditEvent.isSuccessful())
        .remoteAddress(auditEvent.remoteAddress())
        .userAgent(auditEvent.userAgent())
        .build();
  }
}
