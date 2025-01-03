package com.locat.api.unit.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.lost.controller.LostItemController;
import com.locat.api.domain.geo.lost.dto.internal.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.internal.LostItemSearchDto;
import com.locat.api.domain.geo.lost.dto.request.LostItemRegisterRequest;
import com.locat.api.domain.geo.lost.dto.response.LostItemDetailResponse;
import com.locat.api.domain.geo.lost.dto.response.LostItemLocationResponse;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

final class LostItemControllerTest {

  @InjectMocks private LostItemController controller;
  @Mock private LostItemService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("분실물 목록 조회에 성공한다.")
  void testGetFoundItems() {
    // Given
    Long userId = 1L;
    LocatUserDetails userDetails = mock(LocatUserDetails.class);
    LostItemSearchDto searchDto = new LostItemSearchDto(true, null, null);
    Pageable pageable = PageRequest.of(0, 10);
    GeoPage<LostItem> mockResult = mock(GeoPage.class);
    Page<LostItemLocationResponse> responsePage = mock(Page.class);

    given(userDetails.getId()).willReturn(userId);
    given(this.service.findAllByCondition(userId, searchDto, pageable)).willReturn(mockResult);
    given(mockResult.map(any(Function.class))).willReturn(responsePage);

    // When
    ResponseEntity<BaseResponse<Page<LostItemLocationResponse>>> response =
        this.controller.getLostItems(userDetails, searchDto, pageable);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().data()).isEqualTo(responsePage);
    then(this.service).should().findAllByCondition(userId, searchDto, pageable);
  }

  @Test
  @DisplayName("분실물 상세 조회를 성공한다.")
  void testGetFoundItem() {
    // Given
    Long foundItemId = 1L;
    LostItem lostItem =
        LostItem.builder()
            .id(foundItemId)
            .name("name")
            .description("desc")
            .category(Category.builder().name("전자기기").build())
            .colorCodes(Set.of(ColorCode.builder().name("검정색").build()))
            .imageUrl("image")
            .location(GeoUtils.toPoint(123.456, 54.321))
            .isWillingToPayGratuity(true)
            .gratuity(5)
            .lostAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    given(this.service.findById(foundItemId)).willReturn(lostItem);

    // When
    ResponseEntity<BaseResponse<LostItemDetailResponse>> response =
        this.controller.getLostItem(foundItemId);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    then(this.service).should().findById(foundItemId);
  }

  @Test
  @DisplayName("분실물 등록에 성공한다.")
  void testRegister() {
    // Given
    Long userId = 1L;
    String foundItemId = "10";
    LocatUserDetails userDetails = mock(LocatUserDetails.class);
    LostItemRegisterRequest request =
        new LostItemRegisterRequest(1L, Set.of(1L, 2L), "name", "desc", true, 5, 123.456, 54.321);
    MultipartFile mockImage = mock(MultipartFile.class);

    given(userDetails.getId()).willReturn(userId);
    given(this.service.register(eq(userId), any(LostItemRegisterDto.class), eq(mockImage)))
        .willReturn(Long.parseLong(foundItemId));

    // When
    ResponseEntity<BaseResponse<Void>> response =
        this.controller.register(userDetails, request, mockImage);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getLocation())
        .isEqualTo(URI.create("/v1/losts/".concat(foundItemId)));
    then(this.service).should().register(eq(userId), any(LostItemRegisterDto.class), eq(mockImage));
  }
}
