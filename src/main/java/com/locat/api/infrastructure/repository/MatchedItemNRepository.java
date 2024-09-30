package com.locat.api.infrastructure.repository;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

/**
 * 분실물 & 습득물 간 매칭 관련 기능 Repository <br>
 * <h4>매칭 기준</h4>
 * <ul>
 *     <li>카테고리가 동일하고,</li>
 *     <li>색상이 동일하며,</li>
 *     <li>위치가 500m 이내인 아이템</li>
 * </ul>
 * @see #DEFAULT_MATCH_DISTANCE
 * */
public interface MatchedItemNRepository {

  /** 매칭 여부 판단 시, 기준이 되는 거리 */
  Distance DEFAULT_MATCH_DISTANCE = new Distance(0.5, Metrics.KILOMETERS);

  Long countMatchedLostItems(FoundItem foundItem);

  Long countMatchedFoundItems(LostItem lostItem);
}
