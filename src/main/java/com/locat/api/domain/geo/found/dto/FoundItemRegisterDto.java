package com.locat.api.domain.geo.found.dto;

import com.locat.api.domain.geo.found.dto.request.FoundItemRegisterRequest;
import java.util.Set;
import lombok.Builder;
import org.springframework.data.geo.Point;

/**
 * 습득물 등록 DTO
 *
 * @param categoryId 카테고리 ID
 * @param colorIds 색상 ID 목록
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param location 위치 좌표
 */
@Builder
public record FoundItemRegisterDto(
    Long categoryId,
    Set<Long> colorIds,
    String itemName,
    String description,
    String custodyLocation,
    Point location) {

  public static FoundItemRegisterDto from(FoundItemRegisterRequest request) {
    return FoundItemRegisterDto.builder()
        .categoryId(request.categoryId())
        .colorIds(request.colorIds())
        .itemName(request.itemName())
        .description(request.description())
        .custodyLocation(request.custodyLocation())
        .location(new Point(request.lng(), request.lat()))
        .build();
  }
}
