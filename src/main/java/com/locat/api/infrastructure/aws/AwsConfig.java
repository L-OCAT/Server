package com.locat.api.infrastructure.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

  private final AwsProperties awsProperties;

  @Bean
  public S3Client s3Client(AwsBasicCredentials basicCredentials) {
    return S3Client.builder()
        .region(AwsProperties.BASE_REGION)
        .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
        .build();
  }

  @Bean
  public SesClient sesClient(AwsBasicCredentials basicCredentials) {
    return SesClient.builder()
        .region(AwsProperties.BASE_REGION)
        .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
        .build();
  }

  @Bean
  protected AwsBasicCredentials awsBasicCredentials() {
    return AwsBasicCredentials.create(
        this.awsProperties.getAccessKey(), this.awsProperties.getSecretKey());
  }
}
