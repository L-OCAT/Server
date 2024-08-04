package com.locat.api.domain.user.entity;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.core.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "user_oauth",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_oauth",
          columnNames = {"oauth_type", "oauth_id"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOAuth extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "int UNSIGNED")
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type")
  private OAuth2ProviderType oauthType;

  @Size(max = 100)
  @NotNull @Column(name = "oauth_id", nullable = false, length = 100)
  private String oauthId;

  public static UserOAuth from(User user, OAuth2ProviderToken token) {
    return UserOAuth.builder()
        .user(user)
        .oauthType(token.getProviderType())
        .oauthId(token.getId())
        .build();
  }
}
