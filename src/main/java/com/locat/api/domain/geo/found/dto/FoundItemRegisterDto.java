package com.locat.api.domain.geo.found.dto;

import lombok.Builder;
import org.springframework.data.geo.Point;

@Builder
public record FoundItemRegisterDto(
    Boolean isCustomCategory,
    Long categoryId,
    String categoryName,
    Boolean isCustomColor,
    Long colorId,
    String colorName,
    String itemName,
    String description,
    String custodyLocation,
    Point location) {

  public static FoundItemRegisterDto from(FoundItemRegisterRequest request) {
    return FoundItemRegisterDto.builder()
        .isCustomCategory(request.isCustomCategory())
        .categoryId(request.categoryId())
        .categoryName(request.categoryName())
        .isCustomColor(request.isCustomColor())
        .colorId(request.colorId())
        .colorName(request.colorName())
        .itemName(request.itemName())
        .description(request.description())
        .custodyLocation(request.custodyLocation())
        .location(new Point(request.latitude(), request.longitude()))
        .build();
  }
}
