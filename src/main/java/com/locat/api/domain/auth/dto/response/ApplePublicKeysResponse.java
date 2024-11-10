package com.locat.api.domain.auth.dto.response;

import com.locat.api.domain.auth.dto.token.OAuth2ProviderJsonWebKey;
import java.util.List;

public record ApplePublicKeysResponse(List<OAuth2ProviderJsonWebKey> keys) {}
