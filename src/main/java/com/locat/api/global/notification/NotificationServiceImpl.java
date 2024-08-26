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

    @Value("${service.aws.sns.platform-application-arn.ios}")
    private String iosArn;

    @Value("${service.aws.sns.platform-application-arn.android}")
    private String androidArn;
    private final UserRepository userRepository;

    public String createPlatformEndpoint(String token, String platform) {
        String platformApplicationArn = getPlatformApplicationArn(platform);
        CreatePlatformEndpointRequest request = CreatePlatformEndpointRequest.builder()
                .token(token)
                .platformApplicationArn(platformApplicationArn)
                .build();

        try {
            CreatePlatformEndpointResponse response = this.snsClient.createPlatformEndpoint(request);
            return response.endpointArn();
        } catch (SnsException e) {
            throw new RuntimeException("Failed to create platform endpoint", e); // Exception 추가 필요
        }
    }

    public String subscribeEndpointToTopic(String endpointArn) {
        SubscribeRequest request = SubscribeRequest.builder()
                .protocol("application")
                .endpoint(endpointArn)
                .returnSubscriptionArn(true)
                .topicArn(this.topicArn)
                .build();

        try {
            SubscribeResponse response = this.snsClient.subscribe(request);
            return response.subscriptionArn();
        } catch (SnsException e) {
            throw new RuntimeException("Failed to subscribe endpoint to topic", e); // Exception 추가 필요
        }
    }

    private String getPlatformApplicationArn(String platform) {
        if(platform.equalsIgnoreCase("ios")) {
            return this.iosArn;
        } else if (platform.equalsIgnoreCase("android")) {
            return this.androidArn;
        } else {
            throw new IllegalStateException("Unknow platform"); // Exception 추가 필요
        }
    }

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
