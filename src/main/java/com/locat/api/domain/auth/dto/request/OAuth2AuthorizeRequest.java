package com.locat.api.domain.auth.dto.request;

import com.locat.api.domain.user.enums.OAuth2ProviderType;

/**
 * OAuth2 인증 요청 DTO
 *
 * @param providerType OAuth2 제공자 타입
 * @param code 인가 코드
 */
public record OAuth2AuthorizeRequest(OAuth2ProviderType providerType, String code) {}
