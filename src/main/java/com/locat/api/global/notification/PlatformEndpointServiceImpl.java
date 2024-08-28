package com.locat.api.global.notification;

import com.locat.api.domain.user.entity.PlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointRequest;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import static com.locat.api.domain.user.entity.PlatformType.*;

@Service
@RequiredArgsConstructor
public class PlatformEndpointServiceImpl implements PlatformEndpointService{

    private final SnsClient snsClient;

    @Value("${service.aws.sns.platform-application-arn.ios}")
    private String iosArn;

    @Value("${service.aws.sns.platform-application-arn.android}")
    private String androidArn;

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

    private String getPlatformApplicationArn(String platform) {
        if(platform.equalsIgnoreCase(IOS.getValue())) {
            return this.iosArn;
        } else if (platform.equalsIgnoreCase("android")) {
            return this.androidArn;
        } else {
            throw new IllegalStateException("Unknow platform"); // Exception 추가 필요
        }
    }
}
