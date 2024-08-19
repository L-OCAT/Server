package com.locat.api.infrastructure.aws.dynamodb.impl;

import com.locat.api.domain.lost.entity.FoundItem;
import com.locat.api.infrastructure.aws.dynamodb.FoundItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

@Repository
public class FoundItemRepositoryImpl implements FoundItemRepository {

  private final DynamoDbTable<FoundItem> foundDynamoDbTable;

  public FoundItemRepositoryImpl(DynamoDbEnhancedClient dbEnhancedClient) {
    this.foundDynamoDbTable =
        dbEnhancedClient.table("FOUND_ITEM", TableSchema.fromBean(FoundItem.class));
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
    return this.foundDynamoDbTable.scan(r -> r.limit(50)).items().stream().toList();
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
}
