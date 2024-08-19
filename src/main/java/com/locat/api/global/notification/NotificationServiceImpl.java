package com.locat.api.global.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

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

    @Override
    public void sendGeneralNotification(String message, String subject) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(this.topicArn)
                .message(message)
                .subject(subject)
                .build();
        this.snsClient.publish(request);
    }

    @Override
    public void sendUserNotification(Long userId, String message, String subject) {
        PublishRequest request = PublishRequest.builder()
                .targetArn()
                .message(message)
                .subject(subject)
                .messageAttributes()
                .build();
        this.snsClient.publish(request);
    }
}
