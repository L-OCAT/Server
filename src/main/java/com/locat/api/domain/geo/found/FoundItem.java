package com.locat.api.domain.geo.found;

import com.locat.api.domain.user.entity.User;
import com.locat.api.global.annotations.CreatedBy;
import com.locat.api.global.annotations.CreatedDate;
import com.locat.api.global.annotations.LastModifiedBy;
import com.locat.api.global.annotations.LastModifiedDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.geo.Point;
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

  private Point location;

  private ZonedDateTime foundAt;

  private String imageUrl;

  @CreatedDate private ZonedDateTime createdAt;

  @CreatedBy private Long createdBy;

  @LastModifiedDate private ZonedDateTime updatedAt;

  @LastModifiedBy private Long updatedBy;

  @DynamoDbPartitionKey
  public Long getId() {
    return this.id;
  }

  public static FoundItem of(User user, FoundItemRegisterRequest request, String imageUrl) {
    return FoundItem.builder()
        .userId(user.getId())
        .categoryId(request.categoryId())
        .colorId(request.colorId())
        .itemName(request.itemName())
        .description(request.description())
        .custodyLocation(request.custodyLocation())
        .imageUrl(imageUrl)
        .foundAt(ZonedDateTime.now())
        .build();
  }
}
