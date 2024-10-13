package com.locat.api.domain.user.entity;

import com.locat.api.domain.user.dto.OAuth2UserInfoDto;
import com.locat.api.domain.user.entity.association.UserEndpoint;
import com.locat.api.domain.user.entity.association.UserSetting;
import com.locat.api.domain.user.entity.association.UserTermsAgreement;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.global.utils.HashingUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(
    name = "end_user",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_oauth",
          columnNames = {"oauth_type", "oauth_id"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EndUser extends User {

  @Column(name = "oauth_id", nullable = false, length = 100)
  private String oauthId;

  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type")
  private OAuth2ProviderType oauthType;

  @Size(max = 255)
  @Column(name = "profile_image")
  private String profileImage;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserSetting> userSettings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserTermsAgreement> termsAgreements = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserEndpoint> userEndpoints = new ArrayList<>();

  public static EndUser of(String nickname, OAuth2UserInfoDto userInfo) {
    return builder()
        .nickname(nickname)
        .email(userInfo.getEmail())
        .emailHash(HashingUtils.hash(userInfo.getEmail()))
        .oauthId(userInfo.getId())
        .oauthType(userInfo.getProvider())
        .userType(UserType.USER)
        .statusType(StatusType.ACTIVE)
        .build();
  }

  public EndUser update(String email, String nickname) {
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
