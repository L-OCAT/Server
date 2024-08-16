package com.locat.api.unit.file;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.locat.api.global.exception.LocatApiException;
import com.locat.api.global.file.FileOperationFailedException;
import com.locat.api.global.file.FileValidator;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class FileValidatorTest {

  private static final String CONTENT_TYPE = MediaType.TEXT_PLAIN_VALUE;

  @ParameterizedTest(name = "{index} - {2}")
  @MethodSource("provideMockFilesForValidation")
  @DisplayName("FileValidator 테스트")
  void validateFileTest(
      MockMultipartFile file,
      Class<? extends LocatApiException> expectedException,
      String testName) {
    if (expectedException == null) {
      assertThatCode(() -> FileValidator.validate(file)).doesNotThrowAnyException();
    } else {
      assertThatThrownBy(() -> FileValidator.validate(file)).isExactlyInstanceOf(expectedException);
    }
  }

  static Stream<Arguments> provideMockFilesForValidation() {
    return Stream.of(
        Arguments.of(
            new MockMultipartFile(
                "file", "validFile.jpg", CONTENT_TYPE, new byte[1024 * 1024 * 49]),
            null,
            "유효한 파일"),
        Arguments.of(null, FileOperationFailedException.class, "Null 파일"),
        Arguments.of(
            new MockMultipartFile("file", "emptyFile.png", CONTENT_TYPE, new byte[0]),
            FileOperationFailedException.class,
            "빈 파일"),
        Arguments.of(
            new MockMultipartFile(
                "file", "invalidFile.xyz", "application/octet-stream", new byte[1024]),
            FileOperationFailedException.class,
            "지원되지 않는 포맷 파일"),
        Arguments.of(
            new MockMultipartFile(
                "file", "oversizedFile.txt", CONTENT_TYPE, new byte[1024 * 1024 * 51]),
            FileOperationFailedException.class,
            "최대 크기를 넘는 파일"));
  }
}
