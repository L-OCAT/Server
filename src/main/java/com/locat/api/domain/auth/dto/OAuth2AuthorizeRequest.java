package com.locat.api.domain.auth.dto;

import com.locat.api.domain.user.entity.OAuth2ProviderType;

public record OAuth2AuthorizeRequest(OAuth2ProviderType providerType, String code) {}
