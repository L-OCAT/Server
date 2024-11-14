package com.locat.api.infra.aws.s3;

import com.locat.api.infra.aws.exception.FileOperationException;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3 파일 처리 관련 서비스 인터페이스
 *
 * @apiNote 현재는 Bucket Name이 {@code locat}으로 고정되어 있습니다.
 */
public interface LocatS3Client {

  /**
   * {@code MultipartFile}을 S3에 업로드합니다.
   *
   * @param directoryPath 업로드할 디렉토리 경로
   * @param file 업로드할 파일
   * @return S3에 저장된 파일의 키
   * @throws FileOperationException 파일이 존재하지 않거나, 지원하지 않는 확장자, 크기 제한을 초과한 경우 등
   */
  String upload(String directoryPath, MultipartFile file);

  /**
   * S3에 저장된 파일을 삭제합니다.
   *
   * @param key 삭제할 파일의 키
   */
  void delete(String key);
}
