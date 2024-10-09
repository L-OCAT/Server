package com.locat.api.domain.auth.template;

import com.locat.api.domain.user.entity.OAuth2ProviderType;

public interface OAuth2TemplateFactory {

  /**
   * OAuth2ProviderType에 해당하는 OAuth2Template을 반환
   *
   * @param oAuth2ProviderType OAuth2ProviderType
   * @return 선택된 OAuth2Template
   */
  OAuth2Template getByType(final OAuth2ProviderType oAuth2ProviderType);

  /**
   * 현재 사용자의 oAuthId로 OAuth2Template을 반환합니다.
   *
   * @param oAuthId OAuth2ProviderToken의 id
   * @return 선택된 OAuth2Template
   */
  OAuth2Template getById(final String oAuthId);
}
