package com.locat.api.domain.geo.base.service.impl;

import static com.locat.api.global.exception.ApiExceptionType.NOT_FOUND_GEO_ITEM_ADDRESS;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemDetailDto;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchDto;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchQueryResult;
import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.domain.geo.base.dto.kakao.AddressDocument;
import com.locat.api.domain.geo.base.dto.kakao.AddressResponse;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.domain.geo.base.entity.GeoItemType;
import com.locat.api.domain.geo.base.event.GeoItemCreatedEvent;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.base.service.GeoItemAddressService;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.global.exception.custom.InternalProcessingException;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.utils.ValidationUtils;
import com.locat.api.infra.client.http.KakaoGeoClient;
import com.locat.api.infra.persistence.geo.GeoItemAddressRepository;
import com.locat.api.infra.persistence.geo.GeoItemAdminQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class GeoItemAddressServiceImpl implements GeoItemAddressService {

  private final GeoItemAddressRepository geoItemAddressRepository;
  private final GeoItemAdminQRepository geoItemAdminQRepository;
  private final LostItemService lostItemService;
  private final FoundItemService foundItemService;
  private final CategoryService categoryService;
  private final KakaoGeoClient kakaoGeoClient;

  @Override
  public void save(GeoItemAddress geoItemAddress) {
    this.geoItemAddressRepository.save(geoItemAddress);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  @EventListener(value = GeoItemCreatedEvent.class)
  public void handleGeoItemCreatedEvent(GeoItemCreatedEvent event) {
    var geoItem = event.geoItem();
    var point = geoItem.getLocation();
    var addressResponse = this.kakaoGeoClient.getAddress(point.getX(), point.getY());

    this.validateGeocodingResponse(addressResponse);

    this.save(GeoItemAddress.of(event.geoItemType(), geoItem, addressResponse.documents()));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AdminGeoItemSearchDto> findAllByAdminCriteria(
      GeoItemAdminSearchCriteria searchCriteria, Pageable pageable) {
    this.validateRegionHierarchy(
        searchCriteria.region1(), searchCriteria.region2(), searchCriteria.region3());
    return this.geoItemAdminQRepository
        .findAllByAdminCriteria(searchCriteria, pageable)
        .map(this::mapToDto);
  }

  @Override
  @Transactional(readOnly = true)
  public AdminGeoItemDetailDto getGeoItemDetail(Long id) {
    var geoItemAddress =
        this.geoItemAddressRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchEntityException(NOT_FOUND_GEO_ITEM_ADDRESS));

    Long itemId = geoItemAddress.getItemId();
    var geoItem = this.findGeoItemByType(geoItemAddress.getItemType(), itemId);

    var categoryInfoDto = this.getCategoryInfoDto(geoItem.getCategoryId());

    return AdminGeoItemDetailDto.of(geoItem, geoItemAddress, categoryInfoDto);
  }

  private GeoItem findGeoItemByType(GeoItemType itemType, Long itemId) {
    return switch (itemType) {
      case LOST -> this.lostItemService.findById(itemId);
      case FOUND -> this.foundItemService.findById(itemId);
    };
  }

  private CategoryInfoDto getCategoryInfoDto(Long categoryId) {
    return this.categoryService
        .findInfoById(categoryId)
        .orElseThrow(() -> new InternalProcessingException("Failed to fetch category info."));
  }

  private AdminGeoItemSearchDto mapToDto(AdminGeoItemSearchQueryResult queryResult) {
    final long categoryId = queryResult.categoryId();
    CategoryInfoDto categoryInfo = this.getCategoryInfoDto(categoryId);
    return AdminGeoItemSearchDto.of(queryResult, categoryInfo);
  }

  private void validateGeocodingResponse(AddressResponse addressResponse) {
    if (addressResponse == null) {
      throw new InternalProcessingException(
          "Failed to get geocoding response from external(Kakao) API.");
    }

    if (addressResponse.isEmpty()) {
      throw new InvalidParameterException("Location not found.");
    }

    AddressDocument.Address legalRegionDoc = addressResponse.documents()[0].address();
    if (ValidationUtils.isAnyNull(
        legalRegionDoc.region1depthName(),
        legalRegionDoc.region2depthName(),
        legalRegionDoc.region3depthName())) {
      throw new InvalidParameterException("Invalid location: coordinates point to a marine area.");
    }
  }

  private void validateRegionHierarchy(String region1, String region2, String region3) {
    if ((!StringUtils.hasText(region1)
            && (StringUtils.hasText(region2) || StringUtils.hasText(region3)))
        || (!StringUtils.hasText(region2) && StringUtils.hasText(region3))) {
      throw new InvalidParameterException("Invalid region: Region hierarchy is invalid.");
    }
  }
}
