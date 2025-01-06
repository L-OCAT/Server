package com.locat.api.global.security.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 애플리케이션 서비스 관련 설정 정보를 관리하는 클래스
 *
 * @param key Key 관련 설정
 * @param url URL 관련 설정
 * @param admin 관리자 관련 설정
 */
@ConfigurationProperties(prefix = "service")
public record ServiceProperties(Key key, Url url, Admin admin) {

  public boolean isKeyValid(final String key) {
    return this.key.api.equals(key);
  }

  /**
   * 애플리케이션 서비스 Key 관련 설정 정보
   *
   * @param api Public API Key
   * @param encryption 암호화 Key
   */
  public record Key(String api, String encryption) {}

  /**
   * 애플리케이션 서비스 URL 관련 설정 정보
   *
   * @param api API URL
   * @param admin Admin URL
   */
  public record Url(String api, String admin) {}

  /**
   * 관리자 관련 설정 정보
   *
   * @param tempPassword 임시 비밀번호
   */
  public record Admin(String tempPassword) {}
}
