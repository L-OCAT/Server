package com.locat.api.domain.geo.base.service.impl;

import com.locat.api.domain.geo.base.service.MatchedItemService;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.infrastructure.repository.MatchedItemNRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchedItemServiceImpl implements MatchedItemService {

  private final MatchedItemNRepository matchedItemNRepository;
  private final FoundItemService foundItemService;
  private final LostItemService lostItemService;

  @Override
  public Long countMatchedLostItems(Long foundItemId) {
    final FoundItem foundItem = this.foundItemService.findById(foundItemId);
    return this.matchedItemNRepository.countMatchedLostItems(foundItem);
  }

  @Override
  public Long countMatchedFoundItems(Long lostItemId) {
    final LostItem lostItem = this.lostItemService.findById(lostItemId);
    return this.matchedItemNRepository.countMatchedFoundItems(lostItem);
  }
}
