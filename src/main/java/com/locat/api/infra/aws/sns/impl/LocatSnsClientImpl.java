package com.locat.api.infra.aws.sns.impl;

import com.locat.api.domain.user.entity.association.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.AbstractLocatAwsClient;
import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.NotificationException;
import com.locat.api.infra.aws.sns.LocatSnsClient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class LocatSnsClientImpl extends AbstractLocatAwsClient implements LocatSnsClient {

  private static final String MESSAGE_ID_DELIMETER = "|";

  private final SnsClient snsClient;
  private final UserEndpointService userEndpointService;

  public LocatSnsClientImpl(
      AwsProperties awsProperties, SnsClient snsClient, UserEndpointService userEndpointService) {
    super(awsProperties);
    this.snsClient = snsClient;
    this.userEndpointService = userEndpointService;
  }

  @Override
  public String broadcast(String subject, String message) {
    try {
      PublishRequest request =
          PublishRequest.builder()
              .topicArn(this.awsProperties.sns().topicArn())
              .subject(subject)
              .message(message)
              .build();
      return this.snsClient.publish(request).messageId();
    } catch (SnsException e) {
      throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION, e);
    }
  }

  @Override
  public String unicast(Long userId, String subject, String message) {
    Set<String> publishedMessageIds = new HashSet<>();
    List<UserEndpoint> userEndpoints = this.userEndpointService.findUserEndpointsByUserId(userId);

    if (userEndpoints.isEmpty()) {
      throw new NotificationException(ApiExceptionType.NOT_FOUND_ENDPOINT);
    }

    userEndpoints.forEach(
        endpoint -> {
          try {
            PublishRequest request =
                PublishRequest.builder()
                    .targetArn(endpoint.getEndpointArn())
                    .subject(subject)
                    .message(message)
                    .build();
            String publishedMessageId = this.snsClient.publish(request).messageId();
            publishedMessageIds.add(publishedMessageId);
          } catch (SnsException e) {
            throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION, e);
          }
        });
    return String.join(MESSAGE_ID_DELIMETER, publishedMessageIds);
  }
}
