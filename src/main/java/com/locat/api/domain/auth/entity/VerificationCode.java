package com.locat.api.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("VERIFICATION_CODE")
public class VerificationCode {

  @Id private String email;

  private String code;

  @TimeToLive private Long timeToLive;

  public static VerificationCode of(final String email, final String code, final Long timeToLive) {
    return VerificationCode.builder().email(email).code(code).timeToLive(timeToLive).build();
  }
}
