package com.locat.api.integration.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.FileOperationException;
import com.locat.api.infra.aws.s3.impl.LocatS3ClientImpl;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Testcontainers
class LocatS3ClientTest {

  @Container
  private static final LocalStackContainer localStack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
          .withServices(LocalStackContainer.Service.S3);

  @InjectMocks private LocatS3ClientImpl fileService;
  @Mock private S3Client s3Client;
  @Mock private AwsProperties awsProperties;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    AwsProperties.S3 s3 = mock(AwsProperties.S3.class);
    given(this.awsProperties.s3()).willReturn(s3);
    given(s3.bucket()).willReturn("test-bucket");
    given(s3.url()).willReturn("http://localhost:4566");
  }

  @TestFactory
  @DisplayName("FileService 테스트")
  Stream<DynamicTest> fileServiceTests() {
    final MockMultipartFile validFile =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1024]);
    final MockMultipartFile invalidFormatFile =
        new MockMultipartFile("file", "test.txt", "text/plain", new byte[1024]);
    final MockMultipartFile tooLargeFile =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1024 * 1024 * 51]); // 51MB

    return Stream.of(
        this.testUploadValidFile(validFile),
        this.testDeleteValidFile(),
        this.testUploadInvalidFormatFile(invalidFormatFile),
        this.testUploadTooLargeFile(tooLargeFile),
        this.testGetListObjects(),
        this.testS3Exception(validFile));
  }

  private DynamicTest testUploadValidFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "유효한 파일을 업로드하면, 성공적으로 업로드되어야 한다.",
        () -> {
          // When
          String fileName = this.fileService.upload("test-directory", file);

          // Then
          assertThat(fileName).isNotNull();
        });
  }

  private DynamicTest testDeleteValidFile() {
    return DynamicTest.dynamicTest(
        "존재하는 파일을 삭제하면, 성공적으로 삭제되어야 한다.",
        () -> {
          // When
          this.fileService.delete("test-directory/test.jpg");

          // Then
          verify(this.s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
        });
  }

  private DynamicTest testUploadInvalidFormatFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "유효하지 않은 포맷의 파일을 업로드하면, 예외를 던져야 한다.",
        () -> {
          // When & Then
          assertThatThrownBy(() -> this.fileService.upload("test-directory", file))
              .isExactlyInstanceOf(FileOperationException.class);
        });
  }

  private DynamicTest testUploadTooLargeFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "파일 크기가 50MB를 초과하면, 예외를 던져야 한다.",
        () -> {
          // When & Then
          assertThatThrownBy(() -> this.fileService.upload("test-directory", file))
              .isExactlyInstanceOf(FileOperationException.class);
        });
  }

  private DynamicTest testGetListObjects() {
    return DynamicTest.dynamicTest(
        "S3에서 파일 목록을 조회하면 성공적으로 반환되어야 한다.",
        () -> {
          // Given
          ListObjectsV2Response mockResponse =
              ListObjectsV2Response.builder()
                  .contents(
                      List.of(
                          S3Object.builder().key("test-directory/file1.jpg").build(),
                          S3Object.builder().key("test-directory/file2.jpg").build()))
                  .build();

          given(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).willReturn(mockResponse);

          // When
          List<String> result = fileService.getListObjects("test-directory");

          // Then
          assertThat(result)
              .isNotEmpty()
              .containsExactly(
                  "http://localhost:4566/test-directory/file1.jpg",
                  "http://localhost:4566/test-directory/file2.jpg");
        });
  }

  private DynamicTest testS3Exception(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "S3 오류가 발생하면, 예외를 던져야 한다.",
        () -> {
          // Given
          doThrow(S3Exception.builder().message("S3 error").build())
              .when(this.s3Client)
              .putObject(any(PutObjectRequest.class), any(RequestBody.class));

          // When & Then
          assertThatThrownBy(() -> this.fileService.upload("test-directory", file))
              .isExactlyInstanceOf(FileOperationException.class);
        });
  }

  @AfterAll
  static void tearDownAll() {
    localStack.stop();
  }
}
