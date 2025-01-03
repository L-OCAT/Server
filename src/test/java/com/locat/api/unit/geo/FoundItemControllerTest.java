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
import com.locat.api.domain.geo.found.controller.FoundItemController;
import com.locat.api.domain.geo.found.dto.internal.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.internal.FoundItemSearchDto;
import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import com.locat.api.domain.geo.found.dto.response.FoundItemDetailResponse;
import com.locat.api.domain.geo.found.dto.response.FoundItemLocationResponse;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.FoundItemService;
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

final class FoundItemControllerTest {

  @InjectMocks private FoundItemController controller;
  @Mock private FoundItemService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("습득물 목록 조회에 성공한다.")
  void testGetFoundItems() {
    // Given
    Long userId = 1L;
    LocatUserDetails userDetails = mock(LocatUserDetails.class);
    FoundItemSearchDto searchDto = new FoundItemSearchDto(true, null, null);
    Pageable pageable = PageRequest.of(0, 10);
    GeoPage<FoundItem> mockResult = mock(GeoPage.class);
    Page<FoundItemLocationResponse> responsePage = mock(Page.class);

    given(userDetails.getId()).willReturn(userId);
    given(this.service.findAllByCondition(userId, searchDto, pageable)).willReturn(mockResult);
    given(mockResult.map(any(Function.class))).willReturn(responsePage);

    // When
    ResponseEntity<BaseResponse<Page<FoundItemLocationResponse>>> response =
        this.controller.getFoundItems(userDetails, searchDto, pageable);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().data()).isEqualTo(responsePage);
    then(this.service).should().findAllByCondition(userId, searchDto, pageable);
  }

  @Test
  @DisplayName("습득물 상세 조회에 성공한다.")
  void testGetFoundItem() {
    // Given
    Long foundItemId = 1L;
    FoundItem foundItem =
        FoundItem.builder()
            .id(foundItemId)
            .category(Category.builder().name("전자기기").build())
            .colorCodes(Set.of(ColorCode.builder().name("검정색").build()))
            .name("스마트폰")
            .description("잃어버린 스마트폰입니다.")
            .custodyLocation("서울역 분실물센터")
            .imageUrl("http://example.com/image.jpg")
            .location(GeoUtils.toPoint(37.5665, 126.9780))
            .foundAt(LocalDateTime.of(2023, 12, 1, 10, 0))
            .createdAt(LocalDateTime.of(2023, 12, 2, 12, 0))
            .updatedAt(LocalDateTime.of(2023, 12, 3, 14, 0))
            .build();

    given(this.service.findById(foundItemId)).willReturn(foundItem);

    // When
    ResponseEntity<BaseResponse<FoundItemDetailResponse>> response =
        this.controller.getFoundItem(foundItemId);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    then(this.service).should().findById(foundItemId);
  }

  @Test
  @DisplayName("습득물 등록에 성공한다.")
  void testRegister() {
    // Given
    Long userId = 1L;
    String foundItemId = "10";
    LocatUserDetails userDetails = mock(LocatUserDetails.class);
    FoundItemRegisterRequest request =
        new FoundItemRegisterRequest(
            1L, Set.of(1L, 2L), "name", "desc", "location", 123.456, 54.321);
    MultipartFile mockImage = mock(MultipartFile.class);

    given(userDetails.getId()).willReturn(userId);
    given(this.service.register(eq(userId), any(FoundItemRegisterDto.class), eq(mockImage)))
        .willReturn(Long.parseLong(foundItemId));

    // When
    ResponseEntity<BaseResponse<Void>> response =
        this.controller.register(userDetails, request, mockImage);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getLocation())
        .isEqualTo(URI.create("/v1/founds/".concat(foundItemId)));
    then(this.service)
        .should()
        .register(eq(userId), any(FoundItemRegisterDto.class), eq(mockImage));
  }
}
