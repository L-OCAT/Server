package com.locat.api.infrastructure.aws;

import com.locat.api.domain.user.enums.PlatformType;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/** AWS SNS 관련 설정 값을 관리하는 클래스 */
@Configuration
public class AwsSnsProperties {
  /** SNS에서 사용할 기본 프로토콜 */
  public static final String DEFAULT_SNS_PROTOCOL = "application";

  /** SNS를 통해 Broadcast할 Topic의 ARN */
  @Getter private final String topicArn;

  /** 플랫폼별로 Push Notification을 위해 사용할 Platform Application ARN */
  private final Map<PlatformType, String> platformArnMap;

  public AwsSnsProperties(Environment environment) {
    final String messageIfNull = "SNS properties must NOT be null! Check application.yml";
    this.topicArn =
        Objects.requireNonNull(environment.getProperty("service.aws.sns.topic-arn"), messageIfNull);
    Map<PlatformType, String> arnEnumMap = new EnumMap<>(PlatformType.class);
    arnEnumMap.put(
        PlatformType.IOS,
        Objects.requireNonNull(
            environment.getProperty("service.aws.sns.platform-application-arn.ios"),
            messageIfNull));
    arnEnumMap.put(
        PlatformType.ANDROID,
        Objects.requireNonNull(
            environment.getProperty("service.aws.sns.platform-application-arn.android"),
            messageIfNull));
    this.platformArnMap = Collections.unmodifiableMap(arnEnumMap);
  }

  public String getPlatformArn(PlatformType platformType) {
    return this.platformArnMap.get(platformType);
  }
}
