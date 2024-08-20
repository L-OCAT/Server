package com.locat.api.infrastructure.aws.dynamodb.impl;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.FoundItem;
import com.locat.api.infrastructure.aws.dynamodb.FoundItemRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
@Transactional
public class FoundItemRepositoryImpl implements FoundItemRepository {

  private static final String FOUND_ITEM_TABLE = "FOUND_ITEM";

  private final DynamoDbTable<FoundItem> foundDynamoDbTable;

  public FoundItemRepositoryImpl(DynamoDbEnhancedClient dbEnhancedClient) {
    this.foundDynamoDbTable =
        dbEnhancedClient.table(FOUND_ITEM_TABLE, TableSchema.fromBean(FoundItem.class));
  }

  @Override
  public FoundItem save(FoundItem entity) {
    PutItemEnhancedRequest<FoundItem> request =
        PutItemEnhancedRequest.builder(FoundItem.class).item(entity).build();
    return this.foundDynamoDbTable.putItemWithResponse(request).attributes();
  }

  @Override
  public Optional<FoundItem> findById(Long partitionKey) {
    Key key = Key.builder().partitionValue(partitionKey).sortValue(1).build();
    return Optional.ofNullable(this.foundDynamoDbTable.getItem(key));
  }

  @Override
  public List<FoundItem> findAll() {
    return this.foundDynamoDbTable.scan().items().stream().toList();
  }

  @Override
  public Page<FoundItem> findAll(Pageable pageable) {
    final int pageSize = pageable.getPageSize();

    PageIterable<FoundItem> itemPageIterable = this.foundDynamoDbTable.scan(r -> r.limit(pageSize));
    Map<String, AttributeValue> lastEvaluatedKey =
        DynamoDbUtils.getLastEvaluatedKey(itemPageIterable);

    if (lastEvaluatedKey != null) {
      itemPageIterable =
          this.foundDynamoDbTable.scan(r -> r.limit(pageSize).exclusiveStartKey(lastEvaluatedKey));
    }
    // FIXME: lastEvaluatedKey 기반 페이징 제대로

    return new PageImpl<>(DynamoDbUtils.toList(itemPageIterable), pageable, this.countAll());
  }

  @Override
  public FoundItem update(FoundItem entity) {
    UpdateItemEnhancedRequest<FoundItem> request =
        UpdateItemEnhancedRequest.builder(FoundItem.class).item(entity).build();
    return this.foundDynamoDbTable.updateItem(request);
  }

  @Override
  public void delete(Long partitionKey) {
    Key key = Key.builder().partitionValue(partitionKey).sortValue(1).build();
    this.foundDynamoDbTable.deleteItem(key);
  }

  @Override
  public Long countAll() {
    return this.foundDynamoDbTable.scan().stream().count();
  }

  @Override
  public List<FoundItem> findByLocationNear(Point location, Distance distance) {
    return this.findAll().stream()
        .filter(foundItem -> GeoUtils.isInRadius(location, foundItem.getLocation(), distance))
        .toList();
  }
}
