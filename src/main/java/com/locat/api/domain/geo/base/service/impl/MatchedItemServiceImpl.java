package com.locat.api.domain.geo.base.service.impl;

import com.locat.api.domain.geo.base.service.MatchedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchedItemServiceImpl implements MatchedItemService {

  @Override
  public Integer countMatchedLostItems(Long foundItemId) {
    throw new UnsupportedOperationException("Matching is not implemented yet");
  }

  @Override
  public Integer countMatchedFoundItems(Long lostItemId) {
    throw new UnsupportedOperationException("Matching is not implemented yet");
  }
}
