package com.locat.api.domain.user.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class User extends SecuredBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 100)
  @NotNull @Column(name = "oauth_id", nullable = false, length = 100)
  private String oauthId;

  @Size(max = 100)
  @NotNull @Column(name = "email", nullable = false, updatable = false, length = 100)
  private String email;

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
  private ZonedDateTime deletedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserSetting> userSettings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserTermsAgreement> termsAgreements = new ArrayList<>();

  public void delete() {
    this.deletedAt = ZonedDateTime.now();
  }
}
