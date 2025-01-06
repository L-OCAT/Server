package com.locat.api.unit.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.locat.api.global.advice.RequestLoggingAdvice;
import com.locat.api.helper.MockLoggerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class RequestLoggingAdviceTest {

  @InjectMocks private RequestLoggingAdvice advice;
  @Mock private Appender<ILoggingEvent> mockAppender;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.openMocks(this);
    MockLoggerUtils.attachMockAppender(this.mockAppender, this.advice.getClass(), Level.DEBUG);
  }

  @Test
  @DisplayName("DEBUG 레벨이라면, 요청 정보에 대해 로그를 출력해야 한다.")
  void shouldLogRequestInfo() {
    // Given
    MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    mockRequest.setMethod("GET");
    mockRequest.setRequestURI("/api/v1/some");
    mockRequest.addHeader("User-Agent", "MockUserAgent");
    mockRequest.setRemoteAddr("127.0.0.1");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

    // When
    this.advice.debugLogBeforeRequest();

    // Then
    ArgumentCaptor<ILoggingEvent> argumentCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(this.mockAppender, times(1)).doAppend(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues())
        .hasSize(1)
        .allSatisfy(
            event ->
                assertThat(event.getFormattedMessage())
                    .contains("[RequestLog]")
                    .contains("Request: GET /api/v1/some")
                    .contains("User-Agent: MockUserAgent")
                    .contains("From: 127.0.0.1"));
  }
}
