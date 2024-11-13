package com.locat.api.global.file;

import static com.locat.api.global.file.FileServiceImpl.DIRECTORY_DELIMETER;

import com.locat.api.global.exception.ApiExceptionType;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public final class FileUtils {

  private FileUtils() {
    // Utility class
  }

  /**
   * 주어진 디렉토리 경로와 원본 파일 이름을 기반으로 고유한 파일 이름을 생성합니다.
   *
   * <p>파일 이름은 현재 시간을 기반으로 하며, 원본 파일의 확장자를 유지합니다.
   *
   * @param directoryPath 파일이 저장될 디렉토리 경로
   * @param extension 원본 파일의 확장자
   * @return 생성된 파일명
   */
  public static String generateTimeBasedName(String directoryPath, String extension) {
    final long now = System.currentTimeMillis();
    return directoryPath.concat(DIRECTORY_DELIMETER).concat(String.valueOf(now)).concat(extension);
  }

  /**
   * 원본 파일 이름에서 확장자를 추출합니다.
   *
   * @param file 파일
   * @return 파일 확장자
   * @throws FileOperationFailedException 파일 이름에 확장자가 포함되어 있지 않은 경우
   */
  public static String extractExtension(MultipartFile file) {
    final String originalFilename =
        Optional.of(file)
            .map(MultipartFile::getOriginalFilename)
            .map(String::toLowerCase)
            .orElseThrow(
                () ->
                    new FileOperationFailedException(ApiExceptionType.FILE_EXTENSION_NOT_PROVIDED));
    final int lastIndexOfDot = originalFilename.lastIndexOf(".");
    if (lastIndexOfDot == -1 || lastIndexOfDot == originalFilename.length() - 1) {
      throw new FileOperationFailedException(ApiExceptionType.FILE_EXTENSION_NOT_PROVIDED);
    }
    return originalFilename.substring(lastIndexOfDot);
  }
}
