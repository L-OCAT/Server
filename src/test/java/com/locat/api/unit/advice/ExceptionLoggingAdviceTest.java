package com.locat.api.unit.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.locat.api.global.advice.ExceptionLoggingAdvice;
import com.locat.api.helper.MockLoggerUtils;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ExceptionLoggingAdviceTest {

  @InjectMocks private ExceptionLoggingAdvice advice;
  @Mock private Appender<ILoggingEvent> mockAppender;
  @Mock private JoinPoint joinPoint;
  @Mock private Signature signature;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.openMocks(this);

    Mockito.when(joinPoint.getSignature()).thenReturn(this.signature);
    Mockito.when(signature.getDeclaringTypeName()).thenReturn("com.locat.api.SomeClass");
    Mockito.when(signature.getName()).thenReturn("doLog");
    Mockito.when(signature.toShortString()).thenReturn("SomeClass.doLog()");
  }

  @Test
  @DisplayName("예외가 발생하면, 예외 로그를 출력해야 한다.")
  void shouldLogException() {
    // Given
    MockLoggerUtils.attachMockAppender(this.mockAppender, this.advice.getClass(), Level.INFO);
    Throwable ex = new IllegalArgumentException("RootCauseForTest");

    // When
    this.advice.logAfterThrowing(this.joinPoint, ex);

    // Then
    ArgumentCaptor<ILoggingEvent> argumentCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(this.mockAppender, times(1)).doAppend(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues())
        .hasSize(1)
        .allSatisfy(
            event -> {
              assertThat(event.getFormattedMessage())
                  .startsWith("[Exception Log] Resolved exception[IllegalArgumentException]");
              assertThat(event.getFormattedMessage()).contains("Message: RootCauseForTest");
              assertThat(event.getFormattedMessage())
                  .contains("Method: com.locat.api.SomeClass:doLog");
              assertThat(event.getFormattedMessage()).contains("Location: SomeClass.doLog()");
            });
  }

  @Test
  @DisplayName("DEBUG 레벨이라면, RootCause에 대한 로그를 출력해야 한다.")
  void shouldDebugLogRootCause() {
    // Given
    MockLoggerUtils.attachMockAppender(this.mockAppender, this.advice.getClass(), Level.DEBUG);
    Throwable rootCause = new IllegalStateException("RootCauseForTest");
    Throwable ex = new IllegalArgumentException("ExceptionMessage", rootCause);

    // When
    this.advice.logAfterThrowing(this.joinPoint, ex);

    // Then
    ArgumentCaptor<ILoggingEvent> argumentCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(this.mockAppender, times(2)).doAppend(argumentCaptor.capture());
    List<ILoggingEvent> allValues = argumentCaptor.getAllValues();
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(allValues.get(0)) // Exception에 대한 INFO 로그
        .satisfies(
            event ->
                assertThat(event.getFormattedMessage())
                    .startsWith("[Exception Log] Resolved exception[IllegalArgumentException]"));
    assertThat(allValues.get(1)) // RootCause에 대한 DEBUG 로그
        .satisfies(
            event ->
                assertThat(event.getFormattedMessage())
                    .isEqualTo("> Caused by [IllegalStateException] | Message: RootCauseForTest"));
  }
}
