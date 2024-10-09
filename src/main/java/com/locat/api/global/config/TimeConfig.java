package com.locat.api.global.config;

import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

  /** 애플리케이션의 기본 시간대입니다. */
  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

  /**
   * 애플리케이션의 전역 시간대를 설정합니다.
   *
   * @see TimeConfig#DEFAULT_ZONE_ID
   */
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE_ID));
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
