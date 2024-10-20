package com.locat.api.integration.notification;

import static com.locat.api.global.exception.ApiExceptionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.locat.api.domain.user.entity.association.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.global.notification.NotificationException;
import com.locat.api.global.notification.NotificationServiceImpl;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @Container
  private static final LocalStackContainer localStack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
          .withServices(LocalStackContainer.Service.SNS);

  @Mock private UserEndpointService userEndpointService;
  @Mock private UserEndpoint userEndpoint;
  private NotificationServiceImpl notificationService;

  private String endpointArn;

  private static final String TEST_TOPIC_NAME = "integration-test-topic";
  private static final String TEST_SUBJECT = "Test Subject";
  private static final String TEST_MESSAGE = "Test Message";
  private static final Long TEST_USER_ID = 1L;

  @BeforeEach
  void setUp() {
    SnsClient snsClient =
        SnsClient.builder()
            .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SNS))
            .region(Region.of(localStack.getRegion()))
            .credentialsProvider(
                () ->
                    AwsBasicCredentials.create(
                        localStack.getAccessKey(), localStack.getSecretKey()))
            .build();

    CreateTopicResponse topicResponse =
        snsClient.createTopic(CreateTopicRequest.builder().name(TEST_TOPIC_NAME).build());
    String topicArn = topicResponse.topicArn();

    CreatePlatformApplicationResponse platformApplicationResponse =
        snsClient.createPlatformApplication(
            CreatePlatformApplicationRequest.builder()
                .name("test-platform")
                .platform("GCM")
                .attributes(Map.of("PlatformCredential", "test"))
                .build());
    String platformApplicationArn = platformApplicationResponse.platformApplicationArn();

    CreatePlatformEndpointResponse endpointRequest =
        snsClient.createPlatformEndpoint(
            CreatePlatformEndpointRequest.builder()
                .platformApplicationArn(platformApplicationArn)
                .token("device-token")
                .build());
    this.endpointArn = endpointRequest.endpointArn();

    this.notificationService = new NotificationServiceImpl(snsClient, this.userEndpointService);
    ReflectionTestUtils.setField(this.notificationService, "topicArn", topicArn, String.class);
  }

  @Test
  @DisplayName("단체 알림 전송에 성공해야 한다.")
  void testBroadcastSuccess() {
    // When
    String result = this.notificationService.broadcast(TEST_SUBJECT, TEST_MESSAGE);

    // Then
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("개별 알림 전송에 성공해야 한다.")
  void testUnicastSuccess() {
    // Given
    when(this.userEndpoint.getEndpointArn()).thenReturn(this.endpointArn);
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID))
        .thenReturn(List.of(this.userEndpoint));

    // When
    String result = this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE);

    // Then
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("Endpoint가 없는 경우, 개별 알림 전송에 실패해야 한다.")
  void testUnicastNoEndpoint() {
    // Given
    when(this.userEndpointService.findUserEndpointsByUserId(TEST_USER_ID)).thenReturn(List.of());

    // When & Then
    assertThatThrownBy(
            () -> this.notificationService.unicast(TEST_USER_ID, TEST_SUBJECT, TEST_MESSAGE))
        .isInstanceOf(NotificationException.class)
        .hasMessageContaining(NOT_FOUND_ENDPOINT.getMessage());
  }
}
