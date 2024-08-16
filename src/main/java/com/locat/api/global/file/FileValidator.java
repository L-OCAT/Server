package com.locat.api.global.file;

import com.locat.api.global.exception.ApiExceptionType;
import org.springframework.web.multipart.MultipartFile;

import static com.locat.api.global.file.FileServiceImpl.MAX_FILE_SIZE;
import static com.locat.api.global.file.FileServiceImpl.SUPPORTED_FILE_EXTENSION;

public final class FileValidator {

  private FileValidator() {
    // 유틸리티 클래스는 인스턴스화할 수 없습니다.
  }

  /**
   * 파일 업로드 처리 전, 파일의 유효성을 검증합니다.
   *
   * @param multipartFile 검증할 파일
   * @throws FileOperationFailedException 파일이 존재하지 않거나, 지원하지 않는 확장자, 크기 제한을 초과한 경우
   */
  public static void validate(MultipartFile multipartFile) {
    validateFileExistence(multipartFile);
    validateFileFormat(multipartFile);
    validateFileSize(multipartFile.getSize());
  }

  private static void validateFileExistence(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new FileOperationFailedException(ApiExceptionType.NOT_FOUND_FILE);
    }
  }

  private static void validateFileFormat(MultipartFile multipartFile) {
    final String extension = FileUtils.extractExtension(multipartFile);
    if (!SUPPORTED_FILE_EXTENSION.contains(extension)) {
      throw new FileOperationFailedException(ApiExceptionType.FILE_EXTENSION_NOT_SUPPORTED);
    }
  }

  private static void validateFileSize(long size) {
    if (size > MAX_FILE_SIZE.toBytes()) {
      throw new FileOperationFailedException(ApiExceptionType.FILE_SIZE_LIMIT_EXCEEDED);
    }
  }
}
