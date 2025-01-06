package com.locat.api.unit.security.filter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.locat.api.global.security.annotation.PublicApi;
import com.locat.api.global.security.common.AccessLevel;
import com.locat.api.global.security.common.KeyValidation;
import com.locat.api.global.security.common.ServiceProperties;
import com.locat.api.global.security.filter.impl.PublicApiAccessControlFilter;
import com.locat.api.global.web.resolver.HandlerMethodAnnotationResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class PublicApiAccessControlFilterTest {

  private static final String PUBLIC_API_AUTHORIZED = "SECURITY_PUBLIC_API_AUTHORIZED";

  @InjectMocks PublicApiAccessControlFilter filter;
  @Mock ServiceProperties serviceProperties;
  @Mock HandlerMethodAnnotationResolver annotationResolver;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Public API가 아닌 요청이면, 검사 없이 필터 체인을 그대로 진행한다.")
  void proceedFilterChainWhenNotPublicApi() throws Exception {
    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    given(annotationResolver.find(request, PublicApi.class)).willReturn(Optional.empty());

    // when
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // then
    verify(filterChain).doFilter(request, response);
    verify(request, never()).getAttribute(PUBLIC_API_AUTHORIZED);
  }

  @TestFactory
  @DisplayName("Public API 접근 제어 테스트")
  Stream<DynamicTest> publicApiAccessControlTests() {
    return Stream.of(
            new TestCase(
                "완전 공개 API - 인증 성공 처리해야 한다.",
                AccessLevel.PUBLIC,
                KeyValidation.NONE,
                null,
                false,
                true),
            new TestCase(
                "Level - PROTECTED, KeyValidation - REQUIRED, API키가 유효하면 'PublicApi 인증 성공' 처리해야 한다.",
                AccessLevel.PROTECTED,
                KeyValidation.REQUIRED,
                "valid-key",
                true,
                true),
            new TestCase(
                "Level - PROTECTED, KeyValidation - OPTIONAL, API키가 없으면 'PublicApi 인증 성공' 처리해야 한다.",
                AccessLevel.PROTECTED,
                KeyValidation.OPTIONAL,
                null,
                false,
                true),
            new TestCase(
                "Level - PROTECTED, KeyValidation - REQUIRED, API키가 없으면 'PublicApi 인증 성공' 처리하지 않아야 한다.",
                AccessLevel.PROTECTED,
                KeyValidation.REQUIRED,
                null,
                false,
                false),
            new TestCase(
                "Level - PROTECTED, KeyValidation - REQUIRED, API키가 유효하지 않으면 'PublicApi 인증 성공' 처리하지 않아야 한다.",
                AccessLevel.PROTECTED,
                KeyValidation.REQUIRED,
                "invalid-key",
                false,
                false))
        .map(testCase -> DynamicTest.dynamicTest(testCase.displayName, () -> this.run(testCase)));
  }

  private void run(TestCase testCase) throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    PublicApi publicApi = mock(PublicApi.class);

    given(this.annotationResolver.find(request, PublicApi.class))
        .willReturn(Optional.of(publicApi));
    given(publicApi.accessLevel()).willReturn(testCase.accessLevel);
    given(publicApi.keyValidation()).willReturn(testCase.keyValidation);
    given(publicApi.keyHeader()).willReturn("Locat-Api-Key");
    given(request.getHeader("Locat-Api-Key")).willReturn(testCase.apiKey);

    if (testCase.apiKey != null) {
      given(this.serviceProperties.isKeyValid(testCase.apiKey)).willReturn(testCase.keyValid);
    }

    // When
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // Then
    verify(filterChain).doFilter(request, response);
    if (testCase.expectedAuthorized) {
      verify(request).setAttribute(PUBLIC_API_AUTHORIZED, true);
    } else {
      verify(request, never()).getAttribute(PUBLIC_API_AUTHORIZED);
    }
  }

  @Test
  @DisplayName("Public API 설정에 논리적 오류(접근 수준이 PROTECTED이면서 키 검증이 NONE인 경우)라면, 예외를 던져야 한다.")
  void throwExceptionWhenProtectedAccessLevelWithNoneValidation() {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    PublicApi publicApi = mock(PublicApi.class);

    given(this.annotationResolver.find(request, PublicApi.class))
        .willReturn(Optional.of(publicApi));
    given(publicApi.accessLevel()).willReturn(AccessLevel.PROTECTED);
    given(publicApi.keyValidation()).willReturn(KeyValidation.NONE);

    // When & Then
    assertThatThrownBy(
            () ->
                ReflectionTestUtils.invokeMethod(
                    this.filter, "doFilterInternal", request, response, filterChain))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("If access level is PROTECTED, key validation must be REQUIRED or OPTIONAL");
  }

  /** DynamicTest용 테스트 케이스 record */
  private record TestCase(
      String displayName,
      AccessLevel accessLevel,
      KeyValidation keyValidation,
      String apiKey,
      boolean keyValid,
      boolean expectedAuthorized) {}
}
