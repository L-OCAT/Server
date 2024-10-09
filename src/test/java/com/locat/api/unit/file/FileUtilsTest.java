package com.locat.api.unit.file;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.locat.api.global.file.FileOperationFailedException;
import com.locat.api.global.file.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileUtilsTest {

  @Test
  @DisplayName("정상적인 입력의 경우, 새 파일명 생성에 성공해야 한다.")
  void testGenerateFileNameSuccess() {
    // Given
    String directoryPath = "test-directory";
    String originalFilename1 = "file1.txt";
    String originalFilename2 = "file2.jpg";
    String originalFilename3 = "file3.png";

    // When
    String generatedFileName1 = FileUtils.generateTimeBasedName(directoryPath, originalFilename1);
    String generatedFileName2 = FileUtils.generateTimeBasedName(directoryPath, originalFilename2);
    String generatedFileName3 = FileUtils.generateTimeBasedName(directoryPath, originalFilename3);

    // Then
    assertThat(generatedFileName1).startsWith(directoryPath).endsWith(".txt");
    assertThat(generatedFileName2).startsWith(directoryPath).endsWith(".jpg");
    assertThat(generatedFileName3).startsWith(directoryPath).endsWith(".png");
  }

  @Test
  @DisplayName("주어진 파일명에 확장자가 없다면, 예외를 던져야 한다.")
  void testGenerateFileNameFailureNoExtension() {
    // Given
    MockMultipartFile file1 = new MockMultipartFile("file", "file1", "text/plain", new byte[1024]);
    MockMultipartFile file2 = new MockMultipartFile("file", "file2.", "image/jpeg", new byte[1024]);
    MockMultipartFile file3 = new MockMultipartFile("file", "file3", "image/png", new byte[1024]);
    MockMultipartFile file4 =
        new MockMultipartFile("file", new byte[1024]) {
          @Override
          public String getOriginalFilename() {
            return null;
          }
        };

    // When & Then
    assertThatThrownBy(() -> FileUtils.extractExtension(file1))
        .isExactlyInstanceOf(FileOperationFailedException.class);
    assertThatThrownBy(() -> FileUtils.extractExtension(file2))
        .isExactlyInstanceOf(FileOperationFailedException.class);
    assertThatThrownBy(() -> FileUtils.extractExtension(file3))
        .isExactlyInstanceOf(FileOperationFailedException.class);
    assertThatThrownBy(() -> FileUtils.extractExtension(file4))
        .isExactlyInstanceOf(FileOperationFailedException.class);
  }
}
