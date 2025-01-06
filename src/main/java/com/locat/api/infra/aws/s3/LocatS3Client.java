package com.locat.api.infra.aws.s3;

import com.locat.api.infra.aws.exception.FileOperationException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/** S3 파일 처리 관련 서비스 인터페이스 */
public interface LocatS3Client {

  /**
   * S3에 업로드된 객체 목록을 조회합니다.
   *
   * @param directoryPath 조회할 디렉토리 경로({@code items/founds} 형식, 슬래시를 포함하지 않음)
   * @return S3에 저장된 파일 목록
   * @throws FileOperationException 디렉토리가 존재하지 않거나, 권한이 없는 경우 등
   * @apiNote 디렉토리 경로는 반드시 {@code items/founds}와 같은 형식이어야 함. {@code items/founds}와 같은 형태로 경로를 입력해야
   *     합니다.
   */
  List<String> getListObjects(String directoryPath);

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
   * @param key 삭제할 파일의 키(파일명을 포함한 <strong>**버킷 내 전체 경로**</strong>)
   */
  void delete(String key);
}
