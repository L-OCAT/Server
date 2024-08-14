package com.locat.api.global.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${service.aws.access-key}")
  private String accessKey;

  @Value("${service.aws.secret-key}")
  private String secretKey;

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    return S3Client.builder()
        .region(Region.AP_NORTHEAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
        .build();
  }
}
