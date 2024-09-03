package com.locat.api.infrastructure.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "service.aws")
public class AwsProperties {

  /** AWS 서비스에 접근하기 위한 Access Key */
  private String accessKey;

  /** AWS 서비스에 접근하기 위한 Secret Key */
  private String secretKey;

  /** AWS 서비스의 리전 */
  private String region;
}
