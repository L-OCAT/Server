package com.locat.api.global.notification;

import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.exception.ApiExceptionType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private static final String MESSAGE_ID_DELIMETER = "|";

  private final SnsClient snsClient;
  private final UserEndpointService userEndpointService;

  @Value("${service.aws.sns.topic-arn}")
  private String topicArn;

  @Override
  public String broadcast(String subject, String message) {
    try {
      return this.snsClient
          .publish(request -> request.topicArn(this.topicArn).subject(subject).message(message))
          .messageId();
    } catch (SnsException e) {
      throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION);
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
            String publishedMessageId =
                this.snsClient
                    .publish(
                        request ->
                            request
                                .targetArn(endpoint.getEndpointArn())
                                .subject(subject)
                                .message(message))
                    .messageId();
            publishedMessageIds.add(publishedMessageId);
          } catch (SnsException e) {
            throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION);
          }
        });
    return String.join(MESSAGE_ID_DELIMETER, publishedMessageIds);
  }
}
