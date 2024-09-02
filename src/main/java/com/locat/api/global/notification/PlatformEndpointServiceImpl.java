package com.locat.api.global.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import static com.locat.api.domain.user.entity.PlatformType.*;

@Service
@RequiredArgsConstructor
public class PlatformEndpointServiceImpl implements PlatformEndpointService{

    private final SnsClient snsClient;

    @Value("${service.aws.sns.platform-application-arn.ios}")
    private String iosArn;

    @Value("${service.aws.sns.platform-application-arn.android}")
    private String androidArn;

    @Value("${service.aws.sns.topic-arn}")
    private String topicArn;

    private static final String PROTOCOL_APPLICATION = "application";

    @Override
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

    @Override
    public String subscribeEndpointToTopic(String endpointArn) {
        SubscribeRequest request = SubscribeRequest.builder()
                .protocol(PROTOCOL_APPLICATION)
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
        if(platform.equalsIgnoreCase(IOS.getValue())) {
            return this.iosArn;
        } else if (platform.equalsIgnoreCase(ANDROID.getValue())) {
            return this.androidArn;
        } else {
            throw new IllegalStateException("Unknow platform"); // Exception 추가 필요
        }
    }
}
