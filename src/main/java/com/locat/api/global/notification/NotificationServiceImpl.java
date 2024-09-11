package com.locat.api.global.notification;

import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.exception.ApiExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SnsClient snsClient;

    @Value("${service.aws.sns.topic-arn}")
    private String topicArn;

    private final UserEndpointService userEndpointService;

    @Override
    public String sendGeneralNotification(String message, String subject) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(this.topicArn)
                .message(message)
                .subject(subject)
                .build();
        try {
            PublishResponse response = this.snsClient.publish(request);
            return response.messageId();
        } catch (SnsException e) {
            throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION);
        }
    }

    @Override
    public String sendUserNotification(Long userId, String message, String subject) {
        List<UserEndpoint> endpoints = this.userEndpointService.findUserEndpointsByUserId(userId);

        if (endpoints.isEmpty()) {
            throw new NotificationException(ApiExceptionType.NOT_FOUND_ENDPOINT);
        }


        StringBuilder responseMessages = new StringBuilder();
        for (UserEndpoint endpoint : endpoints) {
            PublishRequest request = PublishRequest.builder()
                    .targetArn(endpoint.getEndpointArn())
                    .message(message)
                    .subject(subject)
                    .build();

            try {
                PublishResponse response = this.snsClient.publish(request);
                responseMessages.append(response.messageId());
            } catch (SnsException e) {
                throw new NotificationException(ApiExceptionType.FAIL_TO_SEND_PUSH_NOTIFICATION);
            }
        }
        return responseMessages.toString().trim();
    }
}
