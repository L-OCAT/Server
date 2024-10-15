package com.locat.api.unit.notification;

import static com.locat.api.global.exception.ApiExceptionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.locat.api.domain.user.entity.association.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.notification.NotificationException;
import com.locat.api.global.notification.NotificationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @Mock private SnsClient snsClient;

  @Mock private UserEndpointService userEndpointService;

  @Mock private UserEndpoint userEndpoint1;
  @Mock private UserEndpoint userEndpoint2;

  @InjectMocks private NotificationServiceImpl notificationService;

  private static final String TEST_TOPIC_ARN = "arn:aws:sns::region:account:test-topic-arn";
  private static final String TEST_SUBJECT = "Test Subject";
  private static final String TEST_MESSAGE = "Test Message";
  private static final Long TEST_USER_ID = 1L;
  private static final String TEST_ENDPOINT_ARN1 = "arn:aws:sns:region:account:test-endpoint1";
  private static final String TEST_ENDPOINT_ARN2 = "arn:aws:sns:region:account:test-endpoint2";

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(
        this.notificationService, "topicArn", TEST_TOPIC_ARN, String.class);
  }

  @Test
  @DisplayName("PublishRequest가 올바르게 생성되고, SNS topic에 성공적으로 publish 해야 한다.")
  void testBroadcastSuccess() {
    // Given
    final String messageId = "broadcast123";
    when(this.snsClient.publish(any(PublishRequest.class)))
        .thenReturn(PublishResponse.builder().messageId(messageId).build());

    ArgumentCaptor<PublishRequest> argumentCaptor = ArgumentCaptor.forClass(PublishRequest.class);

    // When
    String result = this.notificationService.broadcast(TEST_SUBJECT, TEST_MESSAGE);

    // Then
    assertThat(result).isEqualTo(messageId);
    verify(this.snsClient, times(1)).publish(argumentCaptor.capture());

    PublishRequest capturedRequest = argumentCaptor.getValue();
    assertThat(capturedRequest.topicArn()).isEqualTo(TEST_TOPIC_ARN);
    assertThat(capturedRequest.subject()).isEqualTo(TEST_SUBJECT);
    assertThat(capturedRequest.message()).isEqualTo(TEST_MESSAGE);
  }

  @Test
  @DisplayName("SnsException이 발생하면, topic에 publish를 실패해야 한다.")
  void testBroadcastFail() {
    // Given
    when(this.snsClient.publish(any(PublishRequest.class)))
        .thenThrow(SnsException.builder().build());

    // When & Then
    assertThatThrownBy(() -> this.notificationService.broadcast(TEST_SUBJECT, TEST_MESSAGE))
        .isInstanceOf(NotificationException.class)
        .hasMessageContaining(FAIL_TO_SEND_PUSH_NOTIFICATION.getMessage());
  }

  @Test
  @DisplayName("하나의 엔드포인트가 있을 때, 성공적으로 메시지를 전송해야 한다.")
  void testSingleUnicastSuccess() {
    // Given
    when(this.userEndpoint1.getEndpointArn()).thenReturn(TEST_ENDPOINT_ARN1);
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID))
        .thenReturn(List.of(this.userEndpoint1));

    final String expectedId = "unicastSingle1";
    when(this.snsClient.publish(any(PublishRequest.class)))
        .thenReturn(PublishResponse.builder().messageId(expectedId).build());

    ArgumentCaptor<PublishRequest> argumentCaptor = ArgumentCaptor.forClass(PublishRequest.class);

    // When
    String result = this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE);

    // Then
    assertThat(result).isEqualTo(expectedId);
    verify(this.userEndpointService, times(1)).findUserEndpointsByUserId(TEST_USER_ID);
    verify(this.snsClient, times(1)).publish(argumentCaptor.capture());

    verifyPublishRequest(argumentCaptor.getValue(), TEST_ENDPOINT_ARN1);
  }

  @Test
  @DisplayName("여러 엔드포인트가 있을 때, 각 엔드포인트에 성공적으로 메시지를 전송해야 한다.")
  void testMultipleEndpointSuccess() {
    // Given
    when(this.userEndpoint1.getEndpointArn()).thenReturn(TEST_ENDPOINT_ARN1);
    when(this.userEndpoint2.getEndpointArn()).thenReturn(TEST_ENDPOINT_ARN2);
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID))
        .thenReturn(List.of(this.userEndpoint1, this.userEndpoint2));

    final String expectedId1 = "unicastMulti1";
    final String expectedId2 = "unicastMulti2";
    when(this.snsClient.publish(any(PublishRequest.class)))
        .thenReturn(PublishResponse.builder().messageId(expectedId1).build())
        .thenReturn(PublishResponse.builder().messageId(expectedId2).build());

    ArgumentCaptor<PublishRequest> argumentCaptor = ArgumentCaptor.forClass(PublishRequest.class);

    // When
    String result = this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE);

    // Then
    assertThat(result).contains(expectedId1, expectedId2);
    verify(this.userEndpointService, times(1)).findUserEndpointsByUserId(TEST_USER_ID);
    verify(this.snsClient, times(2)).publish(argumentCaptor.capture());

    List<PublishRequest> values = argumentCaptor.getAllValues();

    verifyPublishRequest(values.get(0), TEST_ENDPOINT_ARN1);
    verifyPublishRequest(values.get(1), TEST_ENDPOINT_ARN2);
  }

  @Test
  @DisplayName("UserEndpoint가 존재하지 않는다면, 예외를 발생시켜야 한다.")
  void testUserEndpointNotExists() {
    // Given
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID)).thenReturn(List.of());

    // When & Then
    assertThatThrownBy(
            () -> this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE))
        .isInstanceOf(NotificationException.class)
        .hasMessageContaining(NOT_FOUND_ENDPOINT.getMessage());
  }

  @Test
  @DisplayName("SnsException이 발생하면, unicast에 실패해야 한다.")
  void testUnicastFail() {
    // Given
    when(this.userEndpoint1.getEndpointArn()).thenReturn(TEST_ENDPOINT_ARN1);
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID))
        .thenReturn(List.of(this.userEndpoint1));
    when(this.snsClient.publish(any(PublishRequest.class)))
        .thenThrow(SnsException.builder().build());

    // When & Then
    assertThatThrownBy(
            () -> this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE))
        .isInstanceOf(NotificationException.class)
        .hasMessageContaining(FAIL_TO_SEND_PUSH_NOTIFICATION.getMessage());
  }

  private void verifyPublishRequest(PublishRequest request, String targetArn) {
    assertThat(request.targetArn()).isEqualTo(targetArn);
    assertThat(request.subject()).isEqualTo(TEST_SUBJECT);
    assertThat(request.message()).isEqualTo(TEST_MESSAGE);
  }
}
