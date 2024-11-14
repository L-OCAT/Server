package com.locat.api.infra.client.http.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class KakaoOAuth2ClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return request ->
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
  }
}
