package com.locat.api.global.security;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
public class SecurityProperties {

  private final String serviceUrl;
  private final String apiKey;

  public SecurityProperties(Environment environment) {
    this.serviceUrl = environment.getProperty("service.url.api");
    this.apiKey = environment.getProperty("service.api-key");
  }
}
