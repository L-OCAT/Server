package com.locat.api.infra.aws.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * AWS Client 설정 정보를 담는 클래스
 *
 * @param accessKey 서비스 접근을 위한 Access Key
 * @param secretKey 서비스 접근을 위한 Secret Key
 * @param region 서비스가 위치한 리전
 * @param s3 S3 서비스 설정 정보
 * @param ses SES 서비스 설정 정보
 * @param sns SNS 서비스 설정 정보
 */
@ConfigurationProperties(prefix = "service.aws")
public record AwsProperties(
    String accessKey, String secretKey, String region, S3 s3, Ses ses, Sns sns) {

  /**
   * S3 서비스 설정 정보
   *
   * @param bucket S3 버킷 이름
   * @param url S3 서비스 URL
   */
  public record S3(String bucket, String url) {

    /** 서비스에서 지원하는 파일 확장자 */
    public static final List<String> SUPPORTED_FILE_EXTENSION =
        List.of(".jpg", ".jpeg", ".png", ".heic", ".webp");

    /** 비즈니스적으로 설정한 업로드할 수 있는 최대 파일 크기 */
    public static final DataSize MAX_FILE_SIZE = DataSize.ofMegabytes(50);
  }

  /**
   * SES 서비스 설정 정보
   *
   * @param from 메일 발신자
   */
  public record Ses(String from) {

    /** 메일 기본 인코딩 */
    public static final String MAILER_CHARSET = StandardCharsets.UTF_8.name();
  }

  /**
   * SNS 서비스 설정 정보
   *
   * @param topicArn 토픽(Broadcast) ARN
   * @param iosArn iOS ARN
   * @param androidArn Android ARN
   */
  public record Sns(String topicArn, String iosArn, String androidArn) {

    /** SNS에서 사용할 기본 프로토콜 */
    public static final String DEFAULT_SNS_PROTOCOL = "application";
  }
}
