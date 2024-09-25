package com.locat.api.domain.geo.base.service;

/** 분실물 & 습득물 매칭 관련 서비스 */
public interface MatchedItemService {

  /**
   * 매칭 조건에 맞는 분실물 개수 조회
   *
   * @param foundItemId 기준이 되는 습득물 ID
   * @return 매칭된 분실물 개수
   */
  Long countMatchedLostItems(final Long foundItemId);

  /**
   * 매칭 조건에 맞는 습득물 개수 조회
   *
   * @param lostItemId 기준이 되는 분실물 ID
   * @return 매칭된 습득물 개수
   */
  Long countMatchedFoundItems(final Long lostItemId);
}
