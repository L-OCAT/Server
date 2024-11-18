package com.locat.api.domain.geo.lost.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.lost.dto.internal.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.internal.LostItemSearchDto;
import com.locat.api.domain.geo.lost.dto.request.LostItemRegisterRequest;
import com.locat.api.domain.geo.lost.dto.response.LostItemDetailResponse;
import com.locat.api.domain.geo.lost.dto.response.LostItemLocationResponse;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items/losts")
public class LostItemController {

  private final LostItemService lostItemService;

  /** 분실물 목록 조회 */
  @GetMapping
  public ResponseEntity<BaseResponse<Page<LostItemLocationResponse>>> getLostItems(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      final LostItemSearchDto searchDto,
      Pageable pageable) {
    GeoPage<LostItem> result =
        this.lostItemService.findAllByCondition(userDetails.getId(), searchDto, pageable);
    return ResponseEntity.ok(BaseResponse.of(result.map(LostItemLocationResponse::fromEntity)));
  }

  /** 분실물 상세 조회 */
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<LostItemDetailResponse>> getLostItem(
      @PathVariable final Long id) {
    LostItemDetailResponse response =
        LostItemDetailResponse.fromEntity(this.lostItemService.findById(id));
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  /** 분실물 등록 */
  @PostMapping
  public ResponseEntity<BaseResponse<Void>> register(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestPart("request") @Valid LostItemRegisterRequest request,
      @RequestPart(name = "image", required = false) MultipartFile image) {
    final long userId = userDetails.getId();
    final String lostItemId =
        this.lostItemService.register(userId, LostItemRegisterDto.from(request), image).toString();
    return ResponseEntity.created(URI.create("/v1/losts/".concat(lostItemId))).build();
  }
}
