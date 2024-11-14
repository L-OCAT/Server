package com.locat.api.domain.user.service.impl;

import static com.locat.api.infra.aws.config.AwsSnsProperties.DEFAULT_SNS_PROTOCOL;

import com.locat.api.domain.user.enums.PlatformType;
import com.locat.api.domain.user.exception.UserEndpointException;
import com.locat.api.domain.user.service.PlatformEndpointService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.config.AwsSnsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
@RequiredArgsConstructor
public class PlatformEndpointServiceImpl implements PlatformEndpointService {

  private final SnsClient snsClient;
  private final AwsSnsProperties snsProperties;

  @Override
  public String create(String token, PlatformType platformType) {
    String platformApplicationArn = this.snsProperties.getPlatformArn(platformType);
    try {
      CreatePlatformEndpointRequest request =
          CreatePlatformEndpointRequest.builder()
              .token(token)
              .platformApplicationArn(platformApplicationArn)
              .build();
      return this.snsClient.createPlatformEndpoint(request).endpointArn();
    } catch (SnsException e) {
      throw new UserEndpointException(ApiExceptionType.FAIL_TO_CREATE_ENDPOINT);
    }
  }

  @Override
  public String subscribeToTopic(String endpointArn) {
    try {
      SubscribeRequest subscribeRequest =
          SubscribeRequest.builder()
              .protocol(DEFAULT_SNS_PROTOCOL)
              .endpoint(endpointArn)
              .returnSubscriptionArn(true)
              .topicArn(this.snsProperties.getTopicArn())
              .build();
      return this.snsClient.subscribe(subscribeRequest).subscriptionArn();
    } catch (SnsException e) {
      throw new UserEndpointException(ApiExceptionType.FAIL_TO_SUBSCRIBE_TOPIC);
    }
  }
}
