package com.locat.api.domain.geo.base.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.response.AdminGeoItemSearchResponse;
import com.locat.api.domain.geo.base.service.GeoItemAddressService;
import com.locat.api.global.security.annotation.AdminApi;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items")
public class GeoItemController {

  private final GeoItemAddressService geoItemAddressService;

  @AdminApi
  @GetMapping
  public ResponseEntity<BaseResponse<Page<AdminGeoItemSearchResponse>>> findAllByAdminCriteria(
      @RequestParam(required = false) String itemType,
      @RequestParam(required = false) String itemName,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String region1,
      @RequestParam(required = false) String region2,
      @RequestParam(required = false) String region3,
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      Pageable pageable) {
    GeoItemAdminSearchCriteria searchCriteria =
        GeoItemAdminSearchCriteria.of(
            itemType, itemName, region1, region2, region3, categoryId, from, to);
    Page<AdminGeoItemSearchResponse> response =
        geoItemAddressService
            .findAllByAdminCriteria(searchCriteria, pageable)
            .map(AdminGeoItemSearchResponse::from);
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @AdminApi
  @GetMapping("/{index}")
  public ResponseEntity<BaseResponse<GeoItemDetailResponse>> getGeoItemDetail(
          @PathVariable Long index) {
    GeoItemDetailResponse response = geoItemAddressService.getGeoItemDetail(index);
    return ResponseEntity.ok(BaseResponse.of(response));
  }
}
