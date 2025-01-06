package com.locat.api.infra.aws.s3.impl;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.AbstractLocatAwsClient;
import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.FileOperationException;
import com.locat.api.infra.aws.s3.LocatS3Client;
import java.io.IOException;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class LocatS3ClientImpl extends AbstractLocatAwsClient implements LocatS3Client {

  protected static final String DIRECTORY_DELIMETER = "/";

  private static final String ROOT_PATH = "";

  private static final int MAX_OBJECTS_PER_REQUEST = 50;

  private final S3Client s3Client;

  public LocatS3ClientImpl(AwsProperties awsProperties, S3Client s3Client) {
    super(awsProperties);
    this.s3Client = s3Client;
  }

  @Override
  @Cacheable(value = "S3OBJECTS", key = "#directoryPath", unless = "#result.isEmpty()")
  public List<String> getListObjects(String directoryPath) {
    return this.getListObjectsInternal(directoryPath).contents().stream()
        .map(S3Object::key)
        .map(this::buildObjectUrl)
        .toList();
  }

  @Override
  @CacheEvict(value = "S3OBJECTS", key = "#directoryPath", condition = "#result != null")
  public String upload(String directoryPath, MultipartFile file) {
    FileValidator.validate(file);
    final String extension = FileUtils.extractExtension(file);
    final String newFileName = FileUtils.generateTimeBasedName(directoryPath, extension);
    this.putObjectInternal(newFileName, file);
    return this.buildObjectUrl(newFileName);
  }

  @Override
  public void delete(String key) {
    DeleteObjectRequest request =
        DeleteObjectRequest.builder().bucket(this.awsProperties.s3().bucket()).key(key).build();
    this.deleteObjectInternal(request);
  }

  private ListObjectsV2Response getListObjectsInternal(String path) {
    try {
      ListObjectsV2Request request =
          ListObjectsV2Request.builder()
              .bucket(this.awsProperties.s3().bucket())
              .prefix(sanitizePath(path))
              .maxKeys(MAX_OBJECTS_PER_REQUEST)
              .build();
      return this.s3Client.listObjectsV2(request);
    } catch (SdkException e) {
      throw new FileOperationException(ApiExceptionType.S3_ERROR, e);
    }
  }

  private void putObjectInternal(String fileName, MultipartFile file) {
    try {
      PutObjectRequest request =
          PutObjectRequest.builder().bucket(this.awsProperties.s3().bucket()).key(fileName).build();
      this.s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      throw new FileOperationException(ApiExceptionType.FAIL_TO_READ_FILES, e);
    } catch (SdkException e) {
      throw new FileOperationException(ApiExceptionType.S3_ERROR, e);
    }
  }

  private void deleteObjectInternal(DeleteObjectRequest request) {
    try {
      this.s3Client.deleteObject(request);
    } catch (SdkException e) {
      throw new FileOperationException(ApiExceptionType.S3_ERROR, e);
    }
  }

  private String buildObjectUrl(String key) {
    return this.awsProperties.s3().url().concat(DIRECTORY_DELIMETER).concat(key);
  }

  private static String sanitizePath(String path) {
    if (path == null) {
      return ROOT_PATH;
    }
    return path.endsWith(DIRECTORY_DELIMETER) ? path : path.concat(DIRECTORY_DELIMETER);
  }
}
