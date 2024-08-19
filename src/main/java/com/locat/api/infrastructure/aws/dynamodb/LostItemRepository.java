package com.locat.api.infrastructure.aws.dynamodb;

import com.locat.api.domain.lost.entity.LostItem;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public interface LostItemRepository extends DynamoDbCrudRepository<LostItem, Long> {

  List<LostItem> findByLocationNear(Point location, Distance distance);
}
