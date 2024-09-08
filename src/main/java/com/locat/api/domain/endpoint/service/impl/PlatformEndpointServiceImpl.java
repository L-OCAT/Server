package com.locat.api.domain.endpoint.service.impl;

import com.locat.api.domain.endpoint.dto.EndpointRegistrationRequest;
import com.locat.api.domain.endpoint.service.PlatformEndpointService;
import com.locat.api.domain.user.entity.PlatformType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.notification.NotificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;

import static com.locat.api.domain.user.entity.PlatformType.*;

@Service
@RequiredArgsConstructor
public class PlatformEndpointServiceImpl implements PlatformEndpointService {

    private final SnsClient snsClient;

    @Value("${service.aws.sns.platform-application-arn.ios}")
    private String iosArn;

    @Value("${service.aws.sns.platform-application-arn.android}")
    private String androidArn;

    @Value("${service.aws.sns.topic-arn}")
    private String topicArn;
    private static final String DEFAULT_SNS_PROTOCOL = "application";

    private final UserEndpointService userEndpointService;
    private final UserService userService;

    @Override
    public void register(EndpointRegistrationRequest request, LocatUserDetails userDetails) {
        List<UserEndpoint> endpoints = this.userEndpointService.findUserEndpointsByUserId(userDetails.getId());
        boolean endpointExists = this.isEndpointExists(request, endpoints);

        if (!endpointExists) {
            String endpointArn = this.create(
                    request.deviceToken(), request.platform());
            String subscribeArn = this.subscribeToTopic(endpointArn);

            User user = this.userService.findById(userDetails.getId());
            this.userEndpointService.saveUserEndpoint(
                    user, request.deviceToken(), request.platform(), endpointArn);
        }
    }

    @Override
    public String create(String token, String platform) {
        String platformApplicationArn = resolvePlatformArn(platform);
        CreatePlatformEndpointRequest request = CreatePlatformEndpointRequest.builder()
                .token(token)
                .platformApplicationArn(platformApplicationArn)
                .build();

        try {
            CreatePlatformEndpointResponse response = this.snsClient.createPlatformEndpoint(request);
            return response.endpointArn();
        } catch (SnsException e) {
            throw new NotificationException(ApiExceptionType.FAIL_TO_CREATE_ENDPOINT);
        }
    }

    @Override
    public String subscribeToTopic(String endpointArn) {
        SubscribeRequest request = SubscribeRequest.builder()
                .protocol(DEFAULT_SNS_PROTOCOL)
                .endpoint(endpointArn)
                .returnSubscriptionArn(true)
                .topicArn(this.topicArn)
                .build();

        try {
            SubscribeResponse response = this.snsClient.subscribe(request);
            return response.subscriptionArn();
        } catch (SnsException e) {
            throw new NotificationException(ApiExceptionType.FAIL_TO_SUBSCRIBE_TOPIC);
        }
    }

    private String resolvePlatformArn(String platform) {
        final PlatformType platformType = fromValue(platform);
        return switch (platformType) {
            case IOS -> this.iosArn;
            case ANDROID -> this.androidArn;
        };
    }

    private boolean isEndpointExists(EndpointRegistrationRequest request, List<UserEndpoint> endpoints) {
        return endpoints.stream()
                .anyMatch(e -> e.getDeviceToken().equals(request.deviceToken())
                        && e.getPlatformType().getValue().equals(request.platform()));
    }
}
