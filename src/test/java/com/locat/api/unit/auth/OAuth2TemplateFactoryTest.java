package com.locat.api.unit.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.locat.api.domain.auth.template.impl.OAuth2TemplateFactoryImpl;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import com.locat.api.global.security.exception.AuthenticationException;
import com.locat.api.infra.redis.OAuth2ProviderTokenRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class OAuth2TemplateFactoryTest {

  @InjectMocks private OAuth2TemplateFactoryImpl service;
  @Mock private OAuth2ProviderTokenRepository providerTokenRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("OAuth2ProviderType에 해당하는 OAuth2Template을 반환한다.")
  void getByType() {
    // Given
    OAuth2ProviderType providerType = OAuth2ProviderType.APPLE;

    // When & Then
    assertThatCode(() -> this.service.getByType(providerType)).doesNotThrowAnyException();
    then(this.providerTokenRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("현재 사용자가 OAuth 인증되지 않은 경우, AuthenticationException를 던진다.")
  void getByTypeWhenUserNotAuthenticated() {
    // Given
    String oAuthId = "test-oauth-id";
    given(this.providerTokenRepository.findById(oAuthId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.getById(oAuthId))
        .isExactlyInstanceOf(AuthenticationException.class);
    then(this.providerTokenRepository).should().findById(oAuthId);
  }
}
