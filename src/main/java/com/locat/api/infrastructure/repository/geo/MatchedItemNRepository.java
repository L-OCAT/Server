package com.locat.api.infrastructure.repository.geo;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

/**
 * 분실물 & 습득물 간 매칭 관련 기능 Repository <br>
 *
 * <h4>매칭 기준</h4>
 *
 * <ul>
 *   <li>카테고리가 동일하고,
 *   <li>색상이 동일하며,
 *   <li>위치가 500m 이내인 아이템
 * </ul>
 *
 * @see #DEFAULT_MATCH_DISTANCE
 */
public interface MatchedItemNRepository {

  /** 매칭 여부 판단 시, 기준이 되는 거리 */
  Distance DEFAULT_MATCH_DISTANCE = new Distance(0.5, Metrics.KILOMETERS);

  /** 매칭되는 아이템이 없는 경우에 대한 상수 값 */
  Long NONE_MATCHED = 0L;

  /**
   * 분실물에 대해 매칭된 습득물의 개수를 조회합니다.
   *
   * @param foundItem 매칭 여부를 조회할 습득물
   * @return 매칭된 습득물의 개수
   */
  Long countMatchedLostItems(FoundItem foundItem);

  /**
   * 습득물에 대해 매칭된 분실물의 개수를 조회합니다.
   *
   * @param lostItem 매칭 여부를 조회할 분실물
   * @return 매칭된 분실물의 개수
   */
  Long countMatchedFoundItems(LostItem lostItem);
}
