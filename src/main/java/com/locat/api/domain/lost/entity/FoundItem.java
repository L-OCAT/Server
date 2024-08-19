package com.locat.api.domain.lost.entity;

import com.locat.api.global.annotations.CreatedBy;
import com.locat.api.global.annotations.CreatedDate;
import com.locat.api.global.annotations.LastModifiedBy;
import com.locat.api.global.annotations.LastModifiedDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Getter
@Builder
public class FoundItem {

  private Long id;

  private Long userId;

  private Long categoryId;

  private Long colorId;

  private String itemName;

  private String description;

  private String custodyLocation;

  private String foundAt;

  @CreatedDate private ZonedDateTime createdAt;

  @CreatedBy private Long createdBy;

  @LastModifiedDate private ZonedDateTime updatedAt;

  @LastModifiedBy private Long updatedBy;

  @DynamoDbPartitionKey
  public Long getId() {
    return this.id;
  }
}
