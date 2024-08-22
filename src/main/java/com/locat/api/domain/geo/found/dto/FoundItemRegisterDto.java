package com.locat.api.domain.geo.found.dto;

import com.locat.api.domain.geo.base.entity.ColorType;
import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import lombok.Builder;
import org.springframework.data.geo.Point;

/**
 * 습득물 등록 DTO
 *
 * @param categoryId 카테고리 ID(직접 입력한 카테고리일 경우 1)
 * @param categoryName 카테고리 이름
 * @param colorType 색상 타입
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param location 위치 좌표
 */
@Builder
public record FoundItemRegisterDto(
    Long categoryId,
    String categoryName,
    ColorType colorType,
    String itemName,
    String description,
    String custodyLocation,
    Point location) {

  public static FoundItemRegisterDto from(FoundItemRegisterRequest request) {
    return FoundItemRegisterDto.builder()
        .categoryId(request.categoryId())
        .categoryName(request.categoryName())
        .colorType(ColorType.fromHexCode(request.colorHexCode()))
        .itemName(request.itemName())
        .description(request.description())
        .custodyLocation(request.custodyLocation())
        .location(new Point(request.longitude(), request.latitude()))
        .build();
  }
}
