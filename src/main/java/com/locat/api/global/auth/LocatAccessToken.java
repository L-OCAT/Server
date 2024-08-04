package com.locat.api.global.auth;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import static com.locat.api.global.auth.jwt.JwtProviderImpl.ACCESS_TOKEN_EXPIRATION;

@Getter
@Builder
@RedisHash("LOCAT_ACCESS_TOKEN")
public class LocatAccessToken {

  @Id private Long id;
  private String email;
  private String accessToken;
  @TimeToLive private Long accessTokenExpiresIn;

  public static LocatAccessToken from(final Long id, final String email, final String accessToken) {
    return LocatAccessToken.builder()
        .id(id)
        .email(email)
        .accessToken(accessToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .build();
  }
}
