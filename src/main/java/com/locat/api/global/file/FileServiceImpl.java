package com.locat.api.global.file;

import com.locat.api.global.exception.ApiExceptionType;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  protected static final String DIRECTORY_DELIMETER = "/";

  protected static final List<String> SUPPORTED_FILE_EXTENSION =
      List.of(".jpg", ".jpeg", ".png", ".heic", ".webp");

  protected static final DataSize MAX_FILE_SIZE = DataSize.ofMegabytes(50);

  private final S3Client s3Client;

  @Value("${service.aws.s3.bucket}")
  private String bucketName;

  @Value("${service.aws.s3.url}")
  private String bucketUrl;

  @Override
  public String upload(String directoryPath, MultipartFile file) {
    FileValidator.validate(file);
    final String extension = FileUtils.extractExtension(file);
    final String newFileName = FileUtils.generateTimeBasedName(directoryPath, extension);
    PutObjectRequest request =
        PutObjectRequest.builder().bucket(this.bucketName).key(newFileName).build();
    this.putObjectInternal(request, file);
    return buildFileUrl(this.bucketUrl, newFileName);
  }

  @Override
  public void delete(String key) {
    DeleteObjectRequest request =
        DeleteObjectRequest.builder().bucket(this.bucketName).key(key).build();
    this.deleteObjectInternal(request);
  }

  private void putObjectInternal(PutObjectRequest request, MultipartFile file) {
    try {
      this.s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      throw new FileOperationFailedException(ApiExceptionType.FAIL_TO_READ_FILES);
    } catch (SdkException e) {
      throw new FileOperationFailedException(ApiExceptionType.S3_ERROR);
    }
  }

  private void deleteObjectInternal(DeleteObjectRequest request) {
    try {
      this.s3Client.deleteObject(request);
    } catch (SdkException e) {
      throw new FileOperationFailedException(ApiExceptionType.S3_ERROR);
    }
  }

  private static String buildFileUrl(String bucketUrl, String key) {
    return bucketUrl.concat(DIRECTORY_DELIMETER).concat(key);
  }
}
