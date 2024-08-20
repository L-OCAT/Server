package com.locat.api.infrastructure.aws.dynamodb;

import com.locat.api.domain.geo.found.FoundItem;
import java.util.List;

import com.locat.api.domain.geo.found.FoundItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public interface FoundItemRepository extends DynamoDbCrudRepository<FoundItem, Long> {

  List<FoundItem> findByLocationNear(Point location, Distance distance);

  Page<FoundItem> findByCondition(FoundItemSearchDto searchDto, Pageable pageable);
}
