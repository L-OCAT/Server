package com.locat.api;

import com.locat.api.global.config.TimeConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  /**
   * 애플리케이션의 전역 시간대를 설정합니다.
   *
   * @see TimeConfig#DEFAULT_ZONE_ID
   */
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(TimeConfig.DEFAULT_ZONE_ID));
  }
}
