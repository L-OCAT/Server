package com.locat.api.unit.security;

import static com.locat.api.global.security.SecurityConfig.API_KEY_HEADER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.when;

import com.locat.api.global.security.PublicApiKeyFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

class PublicApiKeyFilterTest {

  @InjectMocks PublicApiKeyFilter publicApiKeyFilter;

  @Mock MockFilterChain filterChain;

  @Mock Environment environment;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.filterChain = new MockFilterChain();
    when(environment.getProperty("api.key.property.name")).thenReturn("TesT1Q2w3E");
    ReflectionTestUtils.setField(this.publicApiKeyFilter, "apiKey", "TesT1Q2w3E", String.class);
  }

  @Test
  @DisplayName("적절한 API Key를 제공한 경우, 요청을 통과시킨다.")
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
}
