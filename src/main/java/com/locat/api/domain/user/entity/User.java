package com.locat.api.domain.user.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.global.converter.StringColumnEncryptionConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@SuperBuilder
@Table(
    name = "user",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_email",
          columnNames = {"email_hash"}),
      @UniqueConstraint(
          name = "unique_nickname",
          columnNames = {"nickname"})
    })
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  protected Long id;

  @Size(max = 100)
  @NotNull @Column(name = "email", nullable = false, length = 100)
  @Convert(converter = StringColumnEncryptionConverter.class)
  protected String email;

  @Size(max = 255)
  @NotNull @Column(name = "email_hash", nullable = false)
  protected String emailHash;

  @Size(max = 100)
  @NotNull @Column(name = "nickname", nullable = false, length = 100)
  protected String nickname;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  protected UserType userType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type")
  protected StatusType statusType;

  @LastModifiedBy
  @Column(name = "updated_by")
  protected Long updatedBy;

  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  /** 사용자를 삭제(Soft Delete)합니다. */
  public void delete() {
    this.statusType = StatusType.INACTIVE;
    this.deletedAt = LocalDateTime.now();
  }

  public void updateStatus(StatusType statusType) {
    this.statusType = statusType;
  }

  public boolean isSuperAdmin() {
    return this.userType.isSuperAdmin();
  }

  public boolean isAdmin() {
    return this.userType.isAdmin();
  }

  public String getDeletedAt() {
    return this.deletedAt != null ? this.deletedAt.toString() : null;
  }

  public boolean isNotActivated() {
    return this.statusType != StatusType.ACTIVE;
  }

  public void assertActivated() {
    if (this.isNotActivated()) {
      throw new AccessDeniedException("Access Denied: User is not activated.");
    }
  }
}
