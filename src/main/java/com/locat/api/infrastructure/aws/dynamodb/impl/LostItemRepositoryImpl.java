package com.locat.api.infrastructure.aws.dynamodb.impl;

import com.locat.api.domain.lost.GeoUtils;
import com.locat.api.domain.lost.entity.LostItem;
import com.locat.api.infrastructure.aws.dynamodb.LostItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

@Repository
public class LostItemRepositoryImpl implements LostItemRepository {

  private final DynamoDbTable<LostItem> lostDynamoDbTable;

  public LostItemRepositoryImpl(DynamoDbEnhancedClient dbEnhancedClient) {
    this.lostDynamoDbTable =
        dbEnhancedClient.table("LOST_ITEM", TableSchema.fromBean(LostItem.class));
  }

  @Override
  public LostItem save(LostItem entity) {
    PutItemEnhancedRequest<LostItem> request =
        PutItemEnhancedRequest.builder(LostItem.class).item(entity).build();
    return this.lostDynamoDbTable.putItemWithResponse(request).attributes();
  }

  @Override
  public Optional<LostItem> findById(Long partionKey) {
    Key key = Key.builder().partitionValue(partionKey).sortValue(1).build();
    return Optional.ofNullable(this.lostDynamoDbTable.getItem(key));
  }

  @Override
  public List<LostItem> findAll() {
    return this.lostDynamoDbTable.scan(r -> r.limit(50)).items().stream().toList();
  }

  @Override
  public LostItem update(LostItem entity) {
    UpdateItemEnhancedRequest<LostItem> request =
        UpdateItemEnhancedRequest.builder(LostItem.class).item(entity).build();
    return this.lostDynamoDbTable.updateItem(request);
  }

  @Override
  public void delete(Long partionKey) {
    Key key = Key.builder().partitionValue(partionKey).sortValue(1).build();
    this.lostDynamoDbTable.deleteItem(key);
  }

  @Override
  public List<LostItem> findByLocationNear(Point location, Distance distance) {
    return this.findAll().stream()
        .filter(lostItem -> GeoUtils.isInRadius(location, lostItem.getLocation(), distance))
        .toList();
  }
}
