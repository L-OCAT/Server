package com.locat.api.global.auth;

import com.locat.api.domain.auth.template.OAuth2ProviderTokenDto;
import com.locat.api.domain.user.entity.OAuth2ProviderType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("OAUTH2_PROVIDER_TOKEN")
public class OAuth2ProviderToken {
  @Id private String id;
  private OAuth2ProviderType providerType;
  private String accessToken;
  private String refreshToken;
  private Integer accessTokenExpiresIn;
  private Integer refreshTokenExpiresIn;
  @TimeToLive private Long ttl;

  public static OAuth2ProviderToken from(
      OAuth2ProviderType providerType, String id, OAuth2ProviderTokenDto providerTokenDto) {
    return builder()
        .id(id)
        .providerType(providerType)
        .accessToken(providerTokenDto.getAccessToken())
        .refreshToken(providerTokenDto.getRefreshToken())
        .accessTokenExpiresIn(providerTokenDto.getAccessTokenExpiresIn())
        .refreshTokenExpiresIn(providerTokenDto.getRefreshTokenExpiresIn())
        .ttl(providerTokenDto.getRefreshTokenExpiresIn().longValue())
        .build();
  }
}
