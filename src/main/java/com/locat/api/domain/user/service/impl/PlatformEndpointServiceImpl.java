package com.locat.api.domain.user.service.impl;

import static com.locat.api.domain.user.entity.PlatformType.*;

import com.locat.api.domain.user.entity.PlatformType;
import com.locat.api.domain.user.exception.UserEndpointException;
import com.locat.api.domain.user.service.PlatformEndpointService;
import com.locat.api.global.exception.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
@RequiredArgsConstructor
public class PlatformEndpointServiceImpl implements PlatformEndpointService {

  private final SnsClient snsClient;

  @Value("${service.aws.sns.platform-application-arn.ios}")
  private String iosArn;

  @Value("${service.aws.sns.platform-application-arn.android}")
  private String androidArn;

  @Value("${service.aws.sns.topic-arn}")
  private String topicArn;

  private static final String DEFAULT_SNS_PROTOCOL = "application";

  @Override
  public String create(String token, String platform) {
    String platformApplicationArn = resolvePlatformArn(platform);
    CreatePlatformEndpointRequest request =
        CreatePlatformEndpointRequest.builder()
            .token(token)
            .platformApplicationArn(platformApplicationArn)
            .build();

    try {
      CreatePlatformEndpointResponse response = this.snsClient.createPlatformEndpoint(request);
      return response.endpointArn();
    } catch (SnsException e) {
      throw new UserEndpointException(ApiExceptionType.FAIL_TO_CREATE_ENDPOINT);
    }
  }

  @Override
  public String subscribeToTopic(String endpointArn) {
    SubscribeRequest request =
        SubscribeRequest.builder()
            .protocol(DEFAULT_SNS_PROTOCOL)
            .endpoint(endpointArn)
            .returnSubscriptionArn(true)
            .topicArn(this.topicArn)
            .build();

    try {
      SubscribeResponse response = this.snsClient.subscribe(request);
      return response.subscriptionArn();
    } catch (SnsException e) {
      throw new UserEndpointException(ApiExceptionType.FAIL_TO_SUBSCRIBE_TOPIC);
    }
  }

  private String resolvePlatformArn(String platform) {
    final PlatformType platformType = fromValue(platform);
    return switch (platformType) {
      case IOS -> this.iosArn;
      case ANDROID -> this.androidArn;
    };
  }
}
