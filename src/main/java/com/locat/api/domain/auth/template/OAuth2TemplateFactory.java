package com.locat.api.domain.auth.template;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.auth.template.impl.AppleOAuth2Template;
import com.locat.api.domain.auth.template.impl.KakaoOAuth2Template;
import com.locat.api.domain.user.entity.OAuth2ProviderType;
import com.locat.api.infrastructure.redis.OAuth2ProviderTokenRepository;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OAuth2TemplateFactory {

  private final Map<OAuth2ProviderType, OAuth2Template> oAuth2TemplateMap;
  private final OAuth2ProviderTokenRepository providerTokenRepository;

  /** 불변 EnumMap을 생성해 OAuth2Template을 관리 */
  public OAuth2TemplateFactory(
      OAuth2ProviderTokenRepository providerTokenRepository,
      AppleOAuth2Template appleOAuth2Template,
      KakaoOAuth2Template kakaoOAuth2Template) {
    EnumMap<OAuth2ProviderType, OAuth2Template> map = new EnumMap<>(OAuth2ProviderType.class);
    map.put(OAuth2ProviderType.APPLE, appleOAuth2Template);
    map.put(OAuth2ProviderType.KAKAO, kakaoOAuth2Template);
    this.oAuth2TemplateMap = Collections.unmodifiableMap(map);
    this.providerTokenRepository = providerTokenRepository;
  }

  /**
   * OAuth2ProviderType에 해당하는 OAuth2Template을 반환
   *
   * @param oAuth2ProviderType OAuth2ProviderType
   * @return 선택된 OAuth2Template
   */
  public OAuth2Template getByType(final OAuth2ProviderType oAuth2ProviderType) {
    return this.oAuth2TemplateMap.get(oAuth2ProviderType);
  }

  /**
   * 현재 사용자의 oAuthId로 OAuth2Template을 반환합니다.
   *
   * @param oAuthId OAuth2ProviderToken의 id
   * @return 선택된 OAuth2Template
   * @throws IllegalArgumentException OAuth2ProviderToken이 없을 경우
   */
  public OAuth2Template getById(final String oAuthId) {
    return providerTokenRepository
        .findById(oAuthId)
        .map(OAuth2ProviderToken::getProviderType)
        .map(this::getByType)
        .orElseThrow(() -> new IllegalArgumentException("OAuth2ProviderToken not found"));
  }
}
