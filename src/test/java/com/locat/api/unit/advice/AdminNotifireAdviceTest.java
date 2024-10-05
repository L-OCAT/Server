package com.locat.api.unit.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.locat.api.domain.common.misc.WebhookRequest;
import com.locat.api.global.advice.AdminNotifierAdvice;
import com.locat.api.global.annotation.RequireAdminNotification;
import com.locat.api.global.utils.LocatSpelParser;
import com.locat.api.infrastructure.external.DiscordClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class AdminNotifireAdviceTest {

  @InjectMocks private AdminNotifierAdvice adminNotifierAdvice;
  @Mock private RequireAdminNotification requireAdminNotification;
  @Mock private ProceedingJoinPoint joinPoint;
  @Mock private DiscordClient discordClient;

  @BeforeAll
  static void beforeAll() {
    mockStatic(LocatSpelParser.class);
  }

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(
        this.adminNotifierAdvice, "serverId", "TestServerId", String.class);
    ReflectionTestUtils.setField(
        this.adminNotifierAdvice, "webhookToken", "TestWebhookToken", String.class);
  }

  @Test
  @DisplayName("조건이 true로 평가되면, 알림을 발송해야 한다.")
  void testHandleNotification() throws Throwable {
    // Given
    MethodSignature signature = mock(MethodSignature.class);
    this.setupJoinPoint(signature, "true");

    // When
    when(LocatSpelParser.evaluateExpression(anyString(), any(), any())).thenReturn(true);
    this.adminNotifierAdvice.handleNotification(this.joinPoint, this.requireAdminNotification);

    // Then
    ArgumentCaptor<WebhookRequest> argumentCaptor = ArgumentCaptor.forClass(WebhookRequest.class);
    verify(this.discordClient)
        .send(eq("TestServerId"), eq("TestWebhookToken"), argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().content()).isEqualTo("[관리자 알림] TestMessage");
  }

  @Test
  @DisplayName("조건이 false로 평가되면, 알림을 발송하지 않아야 한다.")
  void testHandleNotificationWithFalseCondition() throws Throwable {
    // Given
    MethodSignature signature = mock(MethodSignature.class);
    this.setupJoinPoint(signature, "false");

    // When
    when(LocatSpelParser.evaluateExpression(anyString(), any(), any())).thenReturn(false);
    this.adminNotifierAdvice.handleNotification(this.joinPoint, this.requireAdminNotification);

    // Then
    verify(this.discordClient, never()).send(anyString(), anyString(), any());
  }

  private void setupJoinPoint(MethodSignature signature, String condition) throws Throwable {
    when(this.joinPoint.getSignature()).thenReturn(signature);
    when(this.requireAdminNotification.message()).thenReturn("TestMessage");
    when(this.requireAdminNotification.condition()).thenReturn(condition);
    when(this.joinPoint.proceed()).thenReturn("TestResult");
  }
}
