package com.locat.api.unit.file;

import com.locat.api.global.file.FileOperationFailedException;
import com.locat.api.global.file.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    assertTrue(generatedFileName1.startsWith(directoryPath));
    assertTrue(generatedFileName1.endsWith(".txt"));

    assertTrue(generatedFileName2.startsWith(directoryPath));
    assertTrue(generatedFileName2.endsWith(".jpg"));

    assertTrue(generatedFileName3.startsWith(directoryPath));
    assertTrue(generatedFileName3.endsWith(".png"));
  }

  @Test
  @DisplayName("주어진 파일명에 확장자가 없다면, 예외를 던져야 한다.")
  void testGenerateFileNameFailureNoExtension() {
    // Given
    String directoryPath = "test-directory";
    String originalFilename1 = "file1";
    String originalFilename2 = "file2.";
    String originalFilename3 = "file3.";

    // When & Then
    assertThrows(
        FileOperationFailedException.class,
        () -> FileUtils.generateTimeBasedName(directoryPath, originalFilename1));
    assertThrows(
        FileOperationFailedException.class,
        () -> FileUtils.generateTimeBasedName(directoryPath, originalFilename2));
    assertThrows(
        FileOperationFailedException.class,
        () -> FileUtils.generateTimeBasedName(directoryPath, originalFilename3));
  }
}
