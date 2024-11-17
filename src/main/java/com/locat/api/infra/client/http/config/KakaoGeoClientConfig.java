package com.locat.api.infra.client.http.config;

import com.locat.api.domain.auth.template.impl.OAuth2Properties;
import com.locat.api.infra.client.http.KakaoGeoClient;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@ConditionalOnClass(KakaoGeoClient.class)
public class KakaoGeoClientConfig {

  private final String kakaoApiKey;

  public KakaoGeoClientConfig(OAuth2Properties oAuth2Properties) {
    this.kakaoApiKey =
        OAuth2Properties.Kakao.ADMIN_KEY_PREFIX + oAuth2Properties.kakao().adminKey();
  }

  @Bean
  public RequestInterceptor kakaoGeoRequestInterceptor() {
    return request -> request.header(HttpHeaders.AUTHORIZATION, this.kakaoApiKey);
  }
}
