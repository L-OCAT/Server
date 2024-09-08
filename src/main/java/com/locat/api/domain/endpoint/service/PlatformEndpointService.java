package com.locat.api.domain.endpoint.service;

import com.locat.api.domain.endpoint.dto.EndpointRegistrationRequest;
import com.locat.api.global.auth.LocatUserDetails;

public interface PlatformEndpointService {

    /**
     * 사용자에게 디바이스 토큰과 플랫폼 정보가 일치하는 endpoint가 있는지 확인하고, 없을 시 생성하여 저장합니다.
     * @param request 디바이스 토큰, 플랫폼 정보
     * @param userDetails 유저 정보
     */
    void register(EndpointRegistrationRequest request, LocatUserDetails userDetails);

    /**
     * 디바이스 토큰과 플랫폼을 기준으로 엔드포인트를 생성합니다.
     * @param token 디바이스 토큰
     * @param platform 플랫폼 - ios, android
     * @return 생성된 디바이스별 엔드포인트 arn
     */
    String create(String token, String platform);

    /**
     * 엔드포인트를 AWS SNS Topic에 구독시킵니다.
     * @param endpointArn 디바이스별 엔드포인트 arn
     * @return 구독 arn
     */
    String subscribeToTopic(String endpointArn);
}
