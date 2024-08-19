package com.locat.api.infrastructure.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "service.aws")
public class AwsProperties {

  /** 서울 Region, LOCAT 서비스의 기본 Region 입니다. */
  public static final Region BASE_REGION = Region.AP_NORTHEAST_2;

  /** AWS 서비스에 접근하기 위한 Access Key */
  private String accessKey;

  /** AWS 서비스에 접근하기 위한 Secret Key */
  private String secretKey;
}
