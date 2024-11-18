package com.locat.api.domain.geo.lost.dto.internal;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.lost.dto.request.LostItemRegisterRequest;
import java.util.Set;
import lombok.Builder;
import org.locationtech.jts.geom.Point;

/**
 * 분실물 등록 DTO
 *
 * @param categoryId 카테고리 ID
 * @param colorIds 색상 ID 목록
 * @param itemName 분실물 이름
 * @param description 분실물 설명
 * @param isWillingToPayGratuity 보상금 지급 여부
 * @param gratuity 보상금 지급 비율
 * @param location 분실 위치 좌표
 */
@Builder
public record LostItemRegisterDto(
    Long categoryId,
    Set<Long> colorIds,
    String itemName,
    String description,
    Boolean isWillingToPayGratuity,
    Integer gratuity,
    Point location) {

  public static LostItemRegisterDto from(LostItemRegisterRequest request) {
    return LostItemRegisterDto.builder()
        .categoryId(request.categoryId())
        .colorIds(request.colorIds())
        .itemName(request.itemName())
        .description(request.description())
        .isWillingToPayGratuity(request.isWillingToPayGratuity())
        .gratuity(request.gratuity())
        .location(GeoUtils.toPoint(request.lat(), request.lng()))
        .build();
  }
}
