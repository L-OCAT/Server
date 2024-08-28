package com.locat.api.global.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Service
@RequiredArgsConstructor
public class TopicSubscriptionServiceImpl implements TopicSubscriptionService {

    private final SnsClient snsClient;

    @Value("${service.aws.sns.topic-arn}")
    private String topicArn;

    @Override
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
}
