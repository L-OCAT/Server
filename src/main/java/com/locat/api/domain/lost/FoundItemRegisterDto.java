package com.locat.api.domain.lost;

public record FoundItemRegisterDto(
    Long categoryId,
    Long colorId,
    String itemName,
    String description,
    String custodyLocation) {

  public static FoundItemRegisterDto from(FoundItemRegisterRequest request) {
    return new FoundItemRegisterDto(
        request.categoryId(),
        request.colorId(),
        request.itemName(),
        request.description(),
        request.custodyLocation());
  }
}
