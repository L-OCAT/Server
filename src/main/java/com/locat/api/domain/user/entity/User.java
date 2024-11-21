package com.locat.api.domain.user.entity;

import com.locat.api.domain.auth.dto.internal.OAuth2UserInfo;
import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.user.entity.association.AdminDeviceId;
import com.locat.api.domain.user.entity.association.UserEndpoint;
import com.locat.api.domain.user.entity.association.UserSetting;
import com.locat.api.domain.user.entity.association.UserTermsAgreement;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.global.persistence.converter.StringColumnEncryptionConverter;
import com.locat.api.global.utils.HashingUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class User extends BaseEntity {

  @Serial private static final long serialVersionUID = 2024111401L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  protected Long id;

  @Column(name = "oauth_id", nullable = false, length = 100)
  private String oauthId;

  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type")
  private OAuth2ProviderType oauthType;

  @Size(max = 255)
  @Column(name = "profile_image")
  private String profileImage;

  @Size(max = 100)
  @NotNull @Column(name = "email", nullable = false, length = 100)
  @Convert(converter = StringColumnEncryptionConverter.class)
  protected String email;

  @Size(max = 255)
  @NotNull @Column(name = "email_hash", nullable = false)
  protected String emailHash;

  @Size(max = 60)
  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "is_password_expired", nullable = false)
  private boolean isPasswordExpired;

  @Size(max = 100)
  @NotNull @Column(name = "nickname", nullable = false, length = 100)
  protected String nickname;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  protected UserType userType;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserSetting> userSettings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserTermsAgreement> termsAgreements = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserEndpoint> userEndpoints = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type")
  protected StatusType statusType;

  @LastModifiedBy
  @Column(name = "updated_by")
  protected Long updatedBy;

  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AdminDeviceId> adminDeviceIds = new ArrayList<>();

  public static User of(
      String profileImage, String nickname, String tempPassword, OAuth2UserInfo userInfo) {
    return builder()
        .oauthId(userInfo.getId())
        .oauthType(userInfo.getProvider())
        .nickname(nickname)
        .profileImage(profileImage)
        .email(userInfo.getEmail())
        .emailHash(HashingUtils.hash(userInfo.getEmail()))
        .password(tempPassword)
        .userType(UserType.USER)
        .statusType(StatusType.ACTIVE)
        .build();
  }

  public User update(String email, String nickname) {
    if (email != null && emailHash != null) {
      this.email = email;
      this.emailHash = HashingUtils.hash(email);
    }
    if (nickname != null) {
      this.nickname = nickname;
    }
    return this;
  }

  public void promote(UserType userType) {
    this.userType = userType;
  }

  public void resetPassword(String encodedPassword) {
    this.password = encodedPassword;
    this.isPasswordExpired = false;
  }

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

  public boolean isTrustedDevice(String deviceId) {
    return this.adminDeviceIds.stream()
        .anyMatch(device -> device.getDeviceId().equals(deviceId) && device.isTrusted());
  }

  public String getDeletedAt() {
    return this.deletedAt != null ? this.deletedAt.toString() : null;
  }

  public boolean isActivated() {
    return this.statusType == StatusType.ACTIVE;
  }

  public void assertActivated() {
    if (!this.isActivated()) {
      throw new AccessDeniedException("Access Denied: User is not activated.");
    }
  }
}
