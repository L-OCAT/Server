package com.locat.api.global.notification;

import com.locat.api.infrastructure.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SnsClient snsClient;

    @Value("${service.aws.sns.topic-arn}")
    private String topicArn;

    private final UserRepository userRepository;

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
        String endpointArn = this.userRepository.findEndpointArnById(userId);
        if (endpointArn == null) {
            throw new RuntimeException("Endpoint Arn not found, userId=" + userId);
        }

        PublishRequest request = PublishRequest.builder()
                .targetArn(endpointArn)
                .message(message)
                .subject(subject)
                .build();
        try {
            PublishResponse response = this.snsClient.publish(request);
            return response.messageId();
        } catch (SnsException e) {
            throw new RuntimeException("Failed to send user notification", e);
        }
    }
}
