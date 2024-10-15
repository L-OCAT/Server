package com.locat.api.integration.notification;

import com.locat.api.global.notification.NotificationServiceImpl;
import org.mockito.InjectMocks;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class NotificationServiceTest {

  @Container
  private static final LocalStackContainer localStack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
          .withServices(LocalStackContainer.Service.SNS);

  @InjectMocks private NotificationServiceImpl notificationService;
}
