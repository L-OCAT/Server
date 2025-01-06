package com.locat.api.domain.user.entity.association;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import com.locat.api.domain.user.dto.internal.EndpointRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.PlatformType;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "user_endpoint")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEndpoint extends SecuredBaseEntity {

  @Serial private static final long serialVersionUID = 2024100901L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "device_token", nullable = false)
  private String deviceToken;

  @Enumerated(EnumType.STRING)
  @Column(name = "platform", nullable = false)
  private PlatformType platformType;

  @Column(name = "endpoint_arn", nullable = false)
  private String endpointArn;

  @Column(name = "subscription_arn", nullable = false)
  private String subscriptionArn;

  public static UserEndpoint of(
      User user, String endpointArn, String subscriptionArn, EndpointRegisterDto registerDto) {
    return UserEndpoint.builder()
        .user(user)
        .deviceToken(registerDto.deviceToken())
        .platformType(registerDto.platformType())
        .endpointArn(endpointArn)
        .subscriptionArn(subscriptionArn)
        .build();
  }

  public boolean matches(String deviceToken, PlatformType platformType) {
    return this.deviceToken.equals(deviceToken) && this.platformType.equals(platformType);
  }
}
