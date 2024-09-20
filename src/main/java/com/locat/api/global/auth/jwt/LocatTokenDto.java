package com.locat.api.global.auth.jwt;

import lombok.Builder;

/**
 * JWT 토큰 발급 응답 DTO
 *
 * @param grantType 토큰 타입(일반적으로 Bearer)
 * @param accessToken 접근 토큰
 * @param refreshToken 갱신 토큰
 * @param accessTokenExpiresIn 접근 토큰 만료 시간 (s)
 * @param refreshTokenExpiresIn 갱신 토큰 만료 시간 (s)
 */
@Builder(builderMethodName = "jwtBuilder", buildMethodName = "create")
public record LocatTokenDto(
    String grantType,
    String accessToken,
    String refreshToken,
    Long accessTokenExpiresIn,
    Long refreshTokenExpiresIn) {}
