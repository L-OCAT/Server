package com.locat.api.domain.user.service;

public interface PlatformEndpointService {

    /**
     * 디바이스 토큰과 플랫폼을 기준으로 엔드포인트를 생성합니다.
     * @param token 디바이스 토큰
     * @param platform 플랫폼 - ios, android
     * @return 생성된 디바이스별 엔드포인트리 arn
     */
    String create(String token, String platform);

    /**
     * 엔드포인트를 AWS SNS Topic에 구독시킵니다.
     * @param endpointArn 디바이스별 엔드포인트 arn
     * @return 구독 arn
     */
    String subscribeToTopic(String endpointArn);
}
