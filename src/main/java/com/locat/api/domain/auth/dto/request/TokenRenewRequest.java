package com.locat.api.domain.auth.dto.request;

/**
 * 토큰 갱신 요청
 *
 * @param accessToken 유효하지만, 만료된 접근 토큰
 * @param refreshToken 갱신 토큰
 */
public record TokenRenewRequest(String accessToken, String refreshToken) {}
