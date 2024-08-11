package com.locat.api.infrastructure.aws.dynamodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

  @Value("${service.aws.dynamodb.access-key}")
  private String accessKey;

  @Value("${service.aws.dynamodb.secret-key}")
  private String secretKey;

  @Bean
  protected DynamoDbEnhancedClient dynamoDbEnhancedClient() {
    return DynamoDbEnhancedClient.builder()
      .dynamoDbClient(this.dynamoDbClient())
      .build();
  }

  @Bean
  protected DynamoDbClient dynamoDbClient() {
    AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(this.accessKey, this.secretKey);
    return DynamoDbClient.builder()
      .region(Region.AP_NORTHEAST_1)
      .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
      .build();
  }
}
