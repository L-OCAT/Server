package com.locat.api.domain.auth.dto.request;

public record TokenRenewRequest(String accessToken, String refreshToken) {}
