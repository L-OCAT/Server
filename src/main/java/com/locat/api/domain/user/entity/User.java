package com.locat.api.domain.user.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.global.converter.StringColumnEncryptionConverter;
import com.locat.api.global.utils.HashingUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@Builder
@Table(
    name = "user",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_oauth_id",
          columnNames = {"oauth_id"}),
      @UniqueConstraint(
          name = "unique_email",
          columnNames = {"email"}),
      @UniqueConstraint(
          name = "unique_nickname",
          columnNames = {"nickname"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "oauth_id", nullable = false, length = 100)
  private String oauthId;

  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type")
  private OAuth2ProviderType oauthType;

  @Size(max = 100)
  @NotNull @Column(name = "email", nullable = false, length = 100)
  @Convert(converter = StringColumnEncryptionConverter.class)
  private String email;

  @Size(max = 255)
  @NotNull @Column(name = "email_hash", nullable = false)
  private String emailHash;

  @Size(max = 100)
  @NotNull @Column(name = "nickname", nullable = false, length = 100)
  private String nickname;

  @Size(max = 255)
  @Column(name = "profile_image")
  private String profileImage;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  private UserType userType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type")
  private StatusType statusType;

  @LastModifiedBy
  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserSetting> userSettings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserTermsAgreement> termsAgreements = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserEndpoint> userEndpoints = new ArrayList<>();

  public static User of(String nickname, OAuth2UserInfoDto userInfo) {
    return User.builder()
        .nickname(nickname)
        .email(userInfo.getEmail())
        .emailHash(HashingUtils.hash(userInfo.getEmail()))
        .oauthId(userInfo.getId())
        .oauthType(userInfo.getProvider())
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

  public void updateStatus(StatusType statusType) {
    this.statusType = statusType;
  }

  public void delete() {
    this.statusType = StatusType.INACTIVE;
    this.deletedAt = LocalDateTime.now();
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
