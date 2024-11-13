package com.locat.api.domain.auth.dto.response;

import com.locat.api.domain.auth.dto.token.OAuth2ProviderJsonWebKey;
import java.util.List;

/**
 * Apple 공개키 응답 DTO
 *
 * @param keys Apple 공개키 목록
 */
public record ApplePublicKeysResponse(List<OAuth2ProviderJsonWebKey> keys) {}
