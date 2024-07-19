package com.locat.api.unit.file;

import com.locat.api.global.file.FileOperationFailedException;
import com.locat.api.global.file.FileServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

  @Container
  private static final LocalStackContainer localStack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
          .withServices(LocalStackContainer.Service.S3);

  @Mock private S3Client s3Client;

  @InjectMocks private FileServiceImpl fileService;

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
        testUploadValidFile(validFile),
        testDeleteValidFile(),
        testUploadInvalidFormatFile(invalidFormatFile),
        testUploadTooLargeFile(tooLargeFile),
        testS3Exception(validFile));
  }

  private DynamicTest testUploadValidFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "유효한 파일을 업로드하면, 성공적으로 업로드되어야 한다.",
        () -> {
          // When
          String fileName = fileService.upload("test-directory", file);

          // Then
          assertNotNull(fileName);
        });
  }

  private DynamicTest testDeleteValidFile() {
    return DynamicTest.dynamicTest(
        "존재하는 파일을 삭제하면, 성공적으로 삭제되어야 한다.",
        () -> {
          // When
          fileService.delete("test-directory/test.jpg");

          // Then
          verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
        });
  }

  private DynamicTest testUploadInvalidFormatFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "유효하지 않은 포맷의 파일을 업로드하면, 예외를 던져야 한다.",
        () -> {
          // When & Then
          assertThrows(
              FileOperationFailedException.class, () -> fileService.upload("test-directory", file));
        });
  }

  private DynamicTest testUploadTooLargeFile(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "파일 크기가 50MB를 초과하면, 예외를 던져야 한다.",
        () -> {
          // When & Then
          assertThrows(
              FileOperationFailedException.class, () -> fileService.upload("test-directory", file));
        });
  }

  private DynamicTest testS3Exception(MockMultipartFile file) {
    return DynamicTest.dynamicTest(
        "S3 오류가 발생하면, 예외를 던져야 한다.",
        () -> {
          // Given
          doThrow(S3Exception.builder().message("S3 error").build())
              .when(s3Client)
              .putObject(any(PutObjectRequest.class), any(RequestBody.class));

          // When & Then
          assertThrows(
              FileOperationFailedException.class, () -> fileService.upload("test-directory", file));
        });
  }

  @AfterAll
  public static void tearDownAll() {
    localStack.stop();
  }
}
