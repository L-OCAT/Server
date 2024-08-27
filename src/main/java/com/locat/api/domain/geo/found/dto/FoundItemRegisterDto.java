package com.locat.api.domain.geo.found.dto;

import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import lombok.Builder;
import org.springframework.data.geo.Point;

/**
 * 습득물 등록 DTO
 *
 * @param categoryId 카테고리 ID
 * @param colorId 색상 ID
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param location 위치 좌표
 */
@Builder
public record FoundItemRegisterDto(
    Long categoryId,
    Long colorId,
    String itemName,
    String description,
    String custodyLocation,
    Point location) {

  public static FoundItemRegisterDto from(FoundItemRegisterRequest request) {
    return FoundItemRegisterDto.builder()
        .categoryId(request.categoryId())
        .colorId(request.colorId())
        .itemName(request.itemName())
        .description(request.description())
        .custodyLocation(request.custodyLocation())
        .location(new Point(request.lng(), request.lat()))
        .build();
  }
}
