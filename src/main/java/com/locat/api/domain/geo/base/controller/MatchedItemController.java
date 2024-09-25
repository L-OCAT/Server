package com.locat.api.domain.geo.base.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.base.dto.GeoItemType;
import com.locat.api.domain.geo.base.service.MatchedItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items")
@PreAuthorize("isAuthenticated()")
public class MatchedItemController {

  private final MatchedItemService matchedItemService;

  /** 매칭된 분실물 / 습득물 개수 조회 */
  @GetMapping("/{type}/{id}/matched-count")
  public ResponseEntity<BaseResponse<Long>> countMatchedLostItems(
      @PathVariable final String type, @PathVariable final Long id) {
    final GeoItemType geoItemType = GeoItemType.fromValue(type);
    final long itemCount =
        switch (geoItemType) {
          case LOSTS -> this.matchedItemService.countMatchedLostItems(id);
          case FOUNDS -> this.matchedItemService.countMatchedFoundItems(id);
        };
    return ResponseEntity.ok(BaseResponse.of(itemCount));
  }
}
