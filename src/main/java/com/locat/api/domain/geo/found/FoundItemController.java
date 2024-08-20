package com.locat.api.domain.geo.found;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.global.auth.LocatUserDetails;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/v1/founds")
public class FoundItemController {

  private final FoundItemService foundItemService;

  /** 습득물 목록 조회 */
  @GetMapping
  public ResponseEntity<BaseResponse<?>> getFoundItems(
      @RequestParam("lat") final Double latitude,
      @RequestParam("lon") final Double longitude,
      @RequestParam("r") final Integer radius,
      @RequestParam final String unit,
      Pageable pageable) {
    FoundItemSearchDto searchDto =
        FoundItemSearchDto.fromRequest(latitude, longitude, radius, unit);
    Object result = this.foundItemService.findAllByCondition(searchDto, pageable);
    return ResponseEntity.ok(BaseResponse.of(result));
  }

  /** 습득물 상세 조회 */
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<FoundItemInfoResponse>> getFoundItem(
      @PathVariable final Long id) {
    FoundItemInfoResponse response =
        FoundItemInfoResponse.fromEntity(this.foundItemService.findById(id));
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  /** 습득물 등록 */
  @PostMapping
  public ResponseEntity<Void> register(
      @AuthenticationPrincipal LocatUserDetails userDetails,
      @RequestBody FoundItemRegisterRequest request,
      @RequestParam MultipartFile foundItemImage) {
    final long userId = userDetails.getId();
    final String foundItemId =
        this.foundItemService.register(userId, request, foundItemImage).toString();
    return ResponseEntity.created(URI.create("/v1/founds/".concat(foundItemId))).build();
  }
}
