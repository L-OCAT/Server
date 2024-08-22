package com.locat.api.domain.geo.lost.dto;

import com.locat.api.domain.geo.base.entity.ColorType;
import com.locat.api.domain.geo.lost.dto.request.LostItemRegisterRequest;
import lombok.Builder;
import org.springframework.data.geo.Point;

/**
 * 분실물 등록 DTO
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param colorType 색상
 * @param itemName 분실물 이름
 * @param description 분실물 설명
 * @param isWillingToPayGratuity 보상금 지급 여부
 * @param gratuity 보상금 지급 비율
 * @param location 분실 위치 좌표
 */
@Builder
public record LostItemRegisterDto(
    Long categoryId,
    String categoryName,
    ColorType colorType,
    String itemName,
    String description,
    Boolean isWillingToPayGratuity,
    Integer gratuity,
    Point location) {

  public static LostItemRegisterDto from(LostItemRegisterRequest request) {
    return LostItemRegisterDto.builder()
        .categoryId(request.categoryId())
        .categoryName(request.categoryName())
        .colorType(ColorType.fromHexCode(request.colorHexCode()))
        .itemName(request.itemName())
        .description(request.description())
        .isWillingToPayGratuity(request.isWillingToPayGratuity())
        .gratuity(request.gratuity())
        .location(new Point(request.longitude(), request.latitude()))
        .build();
  }
}
