package com.locat.api.global.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

  /** 애플리케이션의 기본 시간대입니다. */
  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
