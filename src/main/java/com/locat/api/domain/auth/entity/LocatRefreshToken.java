package com.locat.api.domain.auth.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import static com.locat.api.global.auth.jwt.JwtProviderImpl.REFRESH_TOKEN_EXPIRATION;

@Getter
@Builder
@RedisHash("LOCAT_ACCESS_TOKEN")
public class LocatRefreshToken {

  @Id private Long id;
  private String email;
  private String refreshToken;
  @TimeToLive private Long refreshTokenExpiresIn;

  public static LocatRefreshToken from(
      final Long id, final String email, final String refreshToken) {
    return LocatRefreshToken.builder()
        .id(id)
        .email(email)
        .refreshToken(refreshToken)
        .refreshTokenExpiresIn(REFRESH_TOKEN_EXPIRATION.toSeconds())
        .build();
  }
}
