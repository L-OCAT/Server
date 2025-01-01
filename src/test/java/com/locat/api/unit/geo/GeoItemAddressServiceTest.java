package com.locat.api.unit.geo;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchQueryResult;
import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.domain.geo.base.dto.kakao.AddressDocument;
import com.locat.api.domain.geo.base.dto.kakao.AddressResponse;
import com.locat.api.domain.geo.base.dto.kakao.Meta;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.domain.geo.base.entity.GeoItemType;
import com.locat.api.domain.geo.base.event.GeoItemCreatedEvent;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.base.service.impl.GeoItemAddressServiceImpl;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.global.exception.custom.InternalProcessingException;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.QueryUtils;
import com.locat.api.infra.client.http.KakaoGeoClient;
import com.locat.api.infra.persistence.geo.GeoItemAddressRepository;
import com.locat.api.infra.persistence.geo.GeoItemAdminQRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

final class GeoItemAddressServiceTest {

  @InjectMocks private GeoItemAddressServiceImpl service;
  @Mock private GeoItemAddressRepository repository;
  @Mock private GeoItemAdminQRepository geoItemAdminQRepository;
  @Mock private CategoryService categoryService;
  @Mock private KakaoGeoClient kakaoGeoClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("GeoItemAddress를 저장한다.")
  void testVoid() {
    // Given
    var entity = GeoItemAddress.builder().build();

    // When & Then
    assertThatCode(() -> this.service.save(entity)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("GeoItemCreatedEvent를 구독해 GeoItemAddress를 저장한다.")
  void testHandleGeoItemCreatedEvent() {
    // Given
    var item = LostItem.builder().id(1L).location(GeoUtils.toPoint(37.5665, 126.9780)).build();
    var event = new GeoItemCreatedEvent(GeoItemType.LOST, item);
    var addressResponse =
        new AddressResponse(
            new Meta(1),
            new AddressDocument[] {
              new AddressDocument(
                  new AddressDocument.RoadAddress(
                      "서울특별시 중구 명동길 1", "서울", "중구", "명동", "1", "1", "1", "", "편의점", ""),
                  new AddressDocument.Address(
                      "서울특별시 중구 명동길 1", "서울", "중구", "명동", "Y", "1", "1", ""))
            });
    given(this.kakaoGeoClient.getAddress(126.9780, 37.5665)).willReturn(addressResponse);

    // When & Then
    assertThatCode(() -> this.service.handleGeoItemCreatedEvent(event)).doesNotThrowAnyException();
    then(this.kakaoGeoClient).should().getAddress(126.9780, 37.5665);
    then(this.repository).should().save(any(GeoItemAddress.class));
  }

  @Test
  @DisplayName("좌표를 주소로 변환한 값이 null이면, InternalProcessingException을 던진다.")
  void testHandleGeoItemCreatedEventWithEmptyAddress() {
    // Given
    var item = LostItem.builder().id(1L).location(GeoUtils.toPoint(37.5665, 126.9780)).build();
    var event = new GeoItemCreatedEvent(GeoItemType.LOST, item);
    given(this.kakaoGeoClient.getAddress(126.9780, 37.5665)).willReturn(null);

    // When & Then
    assertThatCode(() -> this.service.handleGeoItemCreatedEvent(event))
        .isExactlyInstanceOf(InternalProcessingException.class);
    then(this.kakaoGeoClient).should().getAddress(126.9780, 37.5665);
    then(this.repository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("좌표를 주소로 변환한 값이 비어있으면, InvalidParameterException을 던진다.")
  void testHandleGeoItemCreatedEventWithEmptyAddressDocuments() {
    // Given
    var item = LostItem.builder().id(1L).location(GeoUtils.toPoint(37.5665, 126.9780)).build();
    var event = new GeoItemCreatedEvent(GeoItemType.LOST, item);
    var addressResponse = new AddressResponse(new Meta(0), new AddressDocument[0]);
    given(this.kakaoGeoClient.getAddress(126.9780, 37.5665)).willReturn(addressResponse);

    // When & Then
    assertThatCode(() -> this.service.handleGeoItemCreatedEvent(event))
        .isExactlyInstanceOf(InvalidParameterException.class);
    then(this.kakaoGeoClient).should().getAddress(126.9780, 37.5665);
    then(this.repository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("좌표를 주소로 변환한 값이 유효하지 않으면, InvalidParameterException을 던진다.")
  void testHandleGeoItemCreatedEventWithInvalidAddress() {
    // Given
    var item = LostItem.builder().id(1L).location(GeoUtils.toPoint(37.5665, 126.9780)).build();
    var event = new GeoItemCreatedEvent(GeoItemType.LOST, item);
    var addressResponse =
        new AddressResponse(
            new Meta(1),
            new AddressDocument[] {
              new AddressDocument(
                  new AddressDocument.RoadAddress(
                      "서울특별시 중구 명동길 1", "서울", "중구", "명동", "1", "1", "1", "", "편의점", ""),
                  new AddressDocument.Address(
                      "서울특별시 중구 명동길 1", "서울", null, null, null, "1", "1", ""))
            });
    given(this.kakaoGeoClient.getAddress(126.9780, 37.5665)).willReturn(addressResponse);

    // When & Then
    assertThatCode(() -> this.service.handleGeoItemCreatedEvent(event))
        .isExactlyInstanceOf(InvalidParameterException.class);
    then(this.kakaoGeoClient).should().getAddress(126.9780, 37.5665);
    then(this.repository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("관리자 검색 조건에 맞는 GeoItem 목록을 조회한다.")
  void testFindAllByAdminCriteria() {
    // Given
    var pageable = PageRequest.of(0, 10);
    var searchCriteria =
        new GeoItemAdminSearchCriteria(GeoItemType.FOUND, null, "서울", "중구", "명동", null, null, null);
    var queryResult =
        new AdminGeoItemSearchQueryResult(
            1L,
            GeoItemType.FOUND,
            "테스트 아이템1",
            8L,
            LocalDateTime.now(),
            BigDecimal.valueOf(37.5665),
            BigDecimal.valueOf(126.9780),
            "서울",
            "중구",
            "명동",
            "",
            "");
    var categoryInfoDto = new CategoryInfoDto(8L, "테스트 카테고리1", 2L, "상위 카테고리");
    given(this.geoItemAdminQRepository.findAllByAdminCriteria(searchCriteria, pageable))
        .willReturn(QueryUtils.toPage(List.of(queryResult), pageable, 1));
    given(this.categoryService.findInfoById(8L)).willReturn(Optional.of(categoryInfoDto));

    // When & Then
    assertThatCode(() -> this.service.findAllByAdminCriteria(searchCriteria, pageable))
        .doesNotThrowAnyException();
    then(this.geoItemAdminQRepository).should().findAllByAdminCriteria(searchCriteria, pageable);
    then(this.categoryService).should().findInfoById(8L);
  }

  @Test
  @DisplayName("검색 조건의 지역 계층이 유효하지 않으면, InvalidParameterException을 던진다.")
  void testFindAllByAdminCriteriaWithInvalidRegionHierarchy() {
    // Given
    var pageable = PageRequest.of(0, 10);
    var searchCriteria =
        new GeoItemAdminSearchCriteria(GeoItemType.FOUND, null, "", null, "명동", null, null, null);

    // When & Then
    assertThatCode(() -> this.service.findAllByAdminCriteria(searchCriteria, pageable))
        .isExactlyInstanceOf(InvalidParameterException.class);
    then(this.geoItemAdminQRepository).shouldHaveNoInteractions();
    then(this.categoryService).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("카테고리 정보를 조회할 수 없으면, InternalProcessingException을 던진다.")
  void testFindAllByAdminCriteriaWithCategoryInfoNotFound() {
    // Given
    var pageable = PageRequest.of(0, 10);
    var searchCriteria =
        new GeoItemAdminSearchCriteria(GeoItemType.FOUND, null, "서울", "중구", "명동", 8L, null, null);
    var queryResult =
        new AdminGeoItemSearchQueryResult(
            1L,
            GeoItemType.FOUND,
            "테스트 아이템1",
            8L,
            LocalDateTime.now(),
            BigDecimal.valueOf(37.5665),
            BigDecimal.valueOf(126.9780),
            "서울",
            "중구",
            "명동",
            "",
            "");
    given(this.geoItemAdminQRepository.findAllByAdminCriteria(searchCriteria, pageable))
        .willReturn(QueryUtils.toPage(List.of(queryResult), pageable, 1));
    given(this.categoryService.findInfoById(8L)).willReturn(Optional.empty());

    // When & Then
    assertThatCode(() -> this.service.findAllByAdminCriteria(searchCriteria, pageable))
        .isExactlyInstanceOf(InternalProcessingException.class);
    then(this.geoItemAdminQRepository).should().findAllByAdminCriteria(searchCriteria, pageable);
    then(this.categoryService).should().findInfoById(8L);
  }
}
