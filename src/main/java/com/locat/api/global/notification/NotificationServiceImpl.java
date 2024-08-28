package com.locat.api.global.notification;

import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
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
            throw new RuntimeException("Failed to send general notification", e); // Exception 추가 필요
        }
    }

    @Override
    public String sendUserNotification(Long userId, String message, String subject) {
        List<UserEndpoint> endpoints = this.userEndpointService.findUserEndpointsByUserId(userId);

        if (endpoints.isEmpty()) {
            throw new RuntimeException("No user endpoint ARN found for userId=" + userId);
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
                throw new RuntimeException("Failed to send user notification", e);
            }
        }
        return responseMessages.toString().trim();
    }
}
