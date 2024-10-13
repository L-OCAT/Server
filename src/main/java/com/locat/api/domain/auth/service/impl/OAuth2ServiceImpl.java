package com.locat.api.domain.auth.service.impl;

import com.locat.api.domain.auth.service.OAuth2Service;
import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

  private final OAuth2TemplateFactory oAuth2TemplateFactory;

  @Override
  public String authenticate(OAuth2ProviderType provider, String code) {
    OAuth2Template oAuth2Template = this.oAuth2TemplateFactory.getByType(provider);
    return oAuth2Template.issueToken(code).getId();
  }

  @Override
  public Boolean isAuthenticated(String oAuthId) {
    return this.oAuth2TemplateFactory.getById(oAuthId).isAuthenticated(oAuthId);
  }
}
