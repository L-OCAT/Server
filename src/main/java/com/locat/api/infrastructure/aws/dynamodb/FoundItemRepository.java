package com.locat.api.infrastructure.aws.dynamodb;

import com.locat.api.domain.lost.entity.FoundItem;

public interface FoundItemRepository extends DynamoDbCrudRepository<FoundItem, Long> {}
