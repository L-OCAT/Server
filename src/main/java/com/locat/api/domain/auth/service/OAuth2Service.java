package com.locat.api.domain.auth.service;

import com.locat.api.domain.user.entity.OAuth2ProviderType;
import com.locat.api.global.auth.jwt.LocatTokenDto;

public interface OAuth2Service {

  LocatTokenDto authenticate(OAuth2ProviderType provider, String code);
}
