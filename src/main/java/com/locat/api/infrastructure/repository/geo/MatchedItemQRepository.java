package com.locat.api.infrastructure.repository.geo;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;

/** 분실물 & 습득물 매칭 QueryDSL Repository */
public interface MatchedItemQRepository {

  long NONE_MATCHED = 0L;

  Long countMatchedLostItems(FoundItem foundItem);

  Long countMatchedFoundItems(LostItem lostItem);
}
