package com.locat.api.domain.geo.found.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import com.locat.api.domain.geo.found.dto.response.FoundItemDetailResponse;
import com.locat.api.domain.geo.found.dto.response.FoundItemLocationResponse;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.global.auth.LocatUserDetails;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items/founds")
@PreAuthorize("isAuthenticated()")
public class FoundItemController {

  private final FoundItemService foundItemService;

  /** 습득물 목록 조회 */
  @GetMapping
  public ResponseEntity<BaseResponse<Page<FoundItemLocationResponse>>> getFoundItems(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      final FoundItemSearchDto searchDto,
      Pageable pageable) {
    GeoPage<FoundItem> result =
        this.foundItemService.findAllByCondition(userDetails.getId(), searchDto, pageable);
    return ResponseEntity.ok(BaseResponse.of(result.map(FoundItemLocationResponse::fromEntity)));
  }

  /** 습득물 상세 조회 */
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<FoundItemDetailResponse>> getFoundItem(
      @PathVariable final Long id) {
    FoundItemDetailResponse response =
        FoundItemDetailResponse.fromEntity(this.foundItemService.findById(id));
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  /** 습득물 등록 */
  @PostMapping
  public ResponseEntity<BaseResponse<Integer>> register(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody @Valid FoundItemRegisterRequest request,
      @RequestParam MultipartFile foundItemImage) {
    final long userId = userDetails.getId();
    final String foundItemId =
        this.foundItemService
            .register(userId, FoundItemRegisterDto.from(request), foundItemImage)
            .toString();
    return ResponseEntity.created(URI.create("/v1/founds/".concat(foundItemId))).build();
  }
}
