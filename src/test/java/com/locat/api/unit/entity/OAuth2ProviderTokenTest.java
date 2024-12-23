package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.auth.dto.token.KakaoOAuth2TokenDto;
import com.locat.api.domain.auth.dto.token.OAuth2ProviderTokenDto;
import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.user.enums.OAuth2ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuth2ProviderTokenTest {

  private static final String ID = "108726732";
  private static final OAuth2ProviderType PROVIDER_TYPE = OAuth2ProviderType.KAKAO;
  private static final String ACCESS_TOKEN = "mock-access-token";
  private static final String REFRESH_TOKEN = "mock-refresh-token";
  private static final String ID_TOKEN = "mock-id-token";
  private static final Integer ACCESS_TOKEN_EXPIRES_IN = 3600;
  private static final Integer REFRESH_TOKEN_EXPIRES_IN = 604800;
  private static final Long TIME_TO_LIVE = 3600L;

  @Test
  @DisplayName("from() 메서드로 OAuth2ProviderToken 객체를 생성할 수 있다.")
  void testFrom() {
    // Given
    OAuth2ProviderTokenDto providerTokenDto =
        new KakaoOAuth2TokenDto(
            "Bearer",
            ACCESS_TOKEN,
            ID_TOKEN,
            ACCESS_TOKEN_EXPIRES_IN,
            REFRESH_TOKEN,
            REFRESH_TOKEN_EXPIRES_IN,
            null);

    // When
    OAuth2ProviderToken result = OAuth2ProviderToken.from(PROVIDER_TYPE, ID, providerTokenDto);

    // Then
    assertAll(
        () -> assertThat(result).isNotNull(),
        () -> assertThat(result.getId()).isEqualTo(ID),
        () -> assertThat(result.getProviderType()).isEqualTo(PROVIDER_TYPE),
        () -> assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN),
        () -> assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN),
        () -> assertThat(result.getIdToken()).isEqualTo(ID_TOKEN),
        () -> assertThat(result.getAccessTokenExpiresIn()).isEqualTo(ACCESS_TOKEN_EXPIRES_IN),
        () -> assertThat(result.getRefreshTokenExpiresIn()).isEqualTo(REFRESH_TOKEN_EXPIRES_IN),
        () -> assertThat(result.getTimeToLive()).isEqualTo(TIME_TO_LIVE));
  }
}
