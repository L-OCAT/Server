package com.locat.api.infrastructure.repository;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

public interface MatchedItemQRepository {

  /** 매칭 여부 판단 시, 기준이 되는 거리 */
  Distance DEFAULT_MATCH_DISTANCE = new Distance(0.5, Metrics.KILOMETERS);

  long NONE_MATCHED = 0L;

  Long countMatchedLostItems(FoundItem foundItem);

  Long countMatchedFoundItems(LostItem lostItem);
}
