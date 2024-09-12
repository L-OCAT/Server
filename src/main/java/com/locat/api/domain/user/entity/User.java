package com.locat.api.domain.user.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.global.security.StringColumnEncryptionConverter;
import com.locat.api.global.utils.HashingUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLSelect;

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
@SQLSelect(sql = "SELECT * FROM user WHERE deleted_at IS NULL")
public class User extends SecuredBaseEntity {

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

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserSetting> userSettings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserTermsAgreement> termsAgreements = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserEndpoint> userEndpoints = new ArrayList<>();

  public static User fromOAuth(OAuth2UserInfoDto userInfo) {
    return User.builder()
        .email(userInfo.getEmail())
        .emailHash(HashingUtils.hash(userInfo.getEmail()))
        .oauthId(userInfo.getId())
        .oauthType(userInfo.getProvider())
        .userType(UserType.USER)
        .statusType(StatusType.ACTIVE)
        .build();
  }

  public boolean isActivated() {
    return this.statusType == StatusType.ACTIVE;
  }

  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }

  public User update(@Email String email, String nickname) {
    if (email != null && emailHash != null) {
      this.email = email;
      this.emailHash = HashingUtils.hash(email);
    }
    if (nickname != null) {
      this.nickname = nickname;
    }
    return this;
  }
}
