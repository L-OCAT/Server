package com.locat.api.unit.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProviderImpl;
import com.locat.api.global.auth.jwt.LocatTokenDto;
import com.locat.api.global.auth.jwt.TokenException;
import com.locat.api.infrastructure.redis.LocatRefreshTokenRepository;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

class JwtProviderTest {

  @InjectMocks private JwtProviderImpl jwtProvider;
  @Mock private Clock clock;
  @Mock private HttpServletRequest request;
  @Mock private LocatUserDetailsService userDetailsService;
  @Mock private LocatRefreshTokenRepository refreshTokenRepository;

  private static final String TEST_SECRET_KEY =
      "mySecretSecretSecretSecretSecretSecretKeySecretKeySecretKeySecretKeymySecretKeymySecretKeymySecretKeyKeyKeyKey";
  private static final String TEST_SERVICE_URL = "http://localhost:8080";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(this.jwtProvider, "secretKey", TEST_SECRET_KEY, String.class);
    ReflectionTestUtils.setField(this.jwtProvider, "serviceUrl", TEST_SERVICE_URL, String.class);
    ReflectionTestUtils.setField(this.jwtProvider, "key", createKey(), Key.class);
    when(this.clock.instant()).thenReturn(Instant.now());
    when(this.clock.getZone()).thenReturn(ZoneId.of("Asia/Seoul"));
  }

  @Test
  @DisplayName("JWT 생성 테스트")
  void shouldCreateTokenDto() {
    // Given
    String userIdStr = "1";
    LocatUserDetails userDetails = mock(LocatUserDetails.class);
    Authentication authentication = mock(Authentication.class);
    when(this.userDetailsService.loadUserByUsername(userIdStr)).thenReturn(userDetails);
    when(this.userDetailsService.createAuthentication(userIdStr)).thenReturn(authentication);
    when(authentication.getName()).thenReturn(userIdStr);

    // When
    LocatTokenDto tokenDto = this.jwtProvider.create(Long.parseLong(userIdStr));

    // Then
    assertThat(tokenDto).isNotNull();
    assertThat(tokenDto.accessToken()).isNotNull();
    assertThat(tokenDto.refreshToken()).isNotNull();
    assertThat(tokenDto.grantType()).isEqualTo("Bearer ");
    assertThat(tokenDto.accessTokenExpiresIn()).isEqualTo(Duration.ofHours(2).toSeconds());
    assertThat(tokenDto.refreshTokenExpiresIn()).isEqualTo(Duration.ofDays(14).toSeconds());
  }

  @Test
  @DisplayName("유효하지 않은 토큰이 주어지면, 예외를 던져야 한다.")
  void shouldThrowExceptionForInvalidToken() {
    // Given
    String invalidToken = "invalidToken";

    // When & Then
    assertThatCode(() -> this.jwtProvider.parse(invalidToken))
        .isInstanceOf(TokenException.class)
        .hasMessage("Unauthorized: Invalid JWT (expired or not matched)");
  }

  @Test
  @DisplayName("HttpRequest에 유효한 형식의 토큰이 주어지면, 토큰을 반환해야 한다.")
  void shouldReturnValidTokenFromRequest() {
    // Given
    this.request = mock(HttpServletRequest.class);
    String bearerToken = JwtProviderImpl.BEARER_PREFIX.concat("validToken");
    when(this.request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(bearerToken);

    // When
    String token = this.jwtProvider.resolve(this.request);

    // Then
    assertThat(token).isNotNull().isEqualTo("validToken");
  }

  @Test
  @DisplayName("HttpRequest에 유효하지 않은 형식의 토큰이 주어지면, null을 반환해야 한다.")
  void shouldReturnNullForInvalidTokenInRequest() {
    // Given
    this.request = mock(HttpServletRequest.class);
    String invalidToken = "InvalidBearerToken";
    when(this.request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(invalidToken);

    // When
    String token = this.jwtProvider.resolve(this.request);

    // Then
    assertThat(token).isNull();
  }

  private static Key createKey() {
    byte[] bytes = Base64.getDecoder().decode(TEST_SECRET_KEY);
    return Keys.hmacShaKeyFor(bytes);
  }
}
