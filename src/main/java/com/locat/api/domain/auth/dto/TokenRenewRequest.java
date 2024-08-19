package com.locat.api.domain.auth.dto;

public record TokenRenewRequest(String accessToken, String refreshToken) {}
