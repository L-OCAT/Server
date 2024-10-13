package com.locat.api.domain.user.dto;

import com.locat.api.domain.user.enums.OAuth2ProviderType;

public interface OAuth2UserInfoDto {

  String getId();

  String getEmail();

  OAuth2ProviderType getProvider();
}
