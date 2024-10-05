package com.locat.api.unit.security;

import static com.locat.api.global.security.filter.AbstractLocatSecurityFilter.API_KEY_HEADER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import com.locat.api.global.exception.NoApiKeyException;
import com.locat.api.global.security.filter.PublicApiKeyFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

class PublicApiKeyFilterTest {

  @InjectMocks private PublicApiKeyFilter publicApiKeyFilter;
  @Mock private MockFilterChain filterChain;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.filterChain = new MockFilterChain();
    ReflectionTestUtils.setField(
        this.publicApiKeyFilter, "publicApiKey", "TesT1Q2w3E", String.class);
  }

  @Test
  @DisplayName("적절한 API Key를 제공한 경우, 요청을 통과 시켜야한다.")
  void testDoFilterInternal_NotPublicApi() {
    // Given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setRequestURI("/v1/auth/some/path");
    request.addHeader(API_KEY_HEADER, "TesT1Q2w3E");

    // When & Then
    assertThatCode(
            () -> this.publicApiKeyFilter.doFilterInternal(request, response, this.filterChain))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("올바르지 않은 API Key를 제공한 경우, 예외를 던져야 한다.")
  void testDoFilterInternal_InvalidApiKey() {
    // Given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setRequestURI("/v1/auth/some/path");
    request.addHeader(API_KEY_HEADER, "InvalidApiKey");

    // When & Then
    assertThatCode(
            () -> this.publicApiKeyFilter.doFilterInternal(request, response, this.filterChain))
        .isExactlyInstanceOf(NoApiKeyException.class);
  }
}
