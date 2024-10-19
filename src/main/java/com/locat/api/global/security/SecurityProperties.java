package com.locat.api.global.security;

import java.util.Objects;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
public class SecurityProperties {

  private final String serviceUrl;
  private final String adminUrl;
  private final String apiKey;

  public SecurityProperties(Environment environment) {
    String errorMessage = "Security properties must not be null";
    this.serviceUrl =
        Objects.requireNonNull(environment.getProperty("service.url.api"), errorMessage);
    this.adminUrl =
        Objects.requireNonNull(environment.getProperty("service.url.admin"), errorMessage);
    this.apiKey = Objects.requireNonNull(environment.getProperty("service.api-key"), errorMessage);
  }
}
