package com.locat.api.infra.aws.s3.impl;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.AbstractLocatAwsClient;
import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.FileOperationException;
import com.locat.api.infra.aws.s3.LocatS3Client;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class LocatS3ClientImpl extends AbstractLocatAwsClient implements LocatS3Client {

  protected static final String DIRECTORY_DELIMETER = "/";

  private final S3Client s3Client;

  public LocatS3ClientImpl(AwsProperties awsProperties, S3Client s3Client) {
    super(awsProperties);
    this.s3Client = s3Client;
  }

  @Override
  public String upload(String directoryPath, MultipartFile file) {
    FileValidator.validate(file);
    final String extension = FileUtils.extractExtension(file);
    final String newFileName = FileUtils.generateTimeBasedName(directoryPath, extension);
    PutObjectRequest request =
        PutObjectRequest.builder()
            .bucket(this.awsProperties.s3().bucket())
            .key(newFileName)
            .build();
    this.putObjectInternal(request, file);
    return buildFileUrl(this.awsProperties.s3().url(), newFileName);
  }

  @Override
  public void delete(String key) {
    DeleteObjectRequest request =
        DeleteObjectRequest.builder().bucket(this.awsProperties.s3().bucket()).key(key).build();
    this.deleteObjectInternal(request);
  }

  private void putObjectInternal(PutObjectRequest request, MultipartFile file) {
    try {
      this.s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      throw new FileOperationException(ApiExceptionType.FAIL_TO_READ_FILES);
    } catch (SdkException e) {
      throw new FileOperationException(ApiExceptionType.S3_ERROR);
    }
  }

  private void deleteObjectInternal(DeleteObjectRequest request) {
    try {
      this.s3Client.deleteObject(request);
    } catch (SdkException e) {
      throw new FileOperationException(ApiExceptionType.S3_ERROR);
    }
  }

  private static String buildFileUrl(String bucketUrl, String key) {
    return bucketUrl.concat(DIRECTORY_DELIMETER).concat(key);
  }
}
