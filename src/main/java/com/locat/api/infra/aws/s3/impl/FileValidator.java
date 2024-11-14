package com.locat.api.infra.aws.s3.impl;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.FileOperationException;
import org.springframework.web.multipart.MultipartFile;

public final class FileValidator {

  private FileValidator() {
    // Utility class
  }

  /**
   * 파일 업로드 처리 전, 파일의 유효성을 검증합니다.
   *
   * @param multipartFile 검증할 파일
   * @throws FileOperationException 파일이 존재하지 않거나, 지원하지 않는 확장자, 크기 제한을 초과한 경우
   */
  public static void validate(MultipartFile multipartFile) {
    validateFileExistence(multipartFile);
    validateFileFormat(multipartFile);
    validateFileSize(multipartFile.getSize());
  }

  private static void validateFileExistence(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new FileOperationException(ApiExceptionType.NOT_FOUND_FILE);
    }
  }

  private static void validateFileFormat(MultipartFile multipartFile) {
    final String extension = FileUtils.extractExtension(multipartFile);
    if (!AwsProperties.S3.SUPPORTED_FILE_EXTENSION.contains(extension)) {
      throw new FileOperationException(ApiExceptionType.FILE_EXTENSION_NOT_SUPPORTED);
    }
  }

  private static void validateFileSize(long size) {
    if (size > AwsProperties.S3.MAX_FILE_SIZE.toBytes()) {
      throw new FileOperationException(ApiExceptionType.FILE_SIZE_LIMIT_EXCEEDED);
    }
  }
}
