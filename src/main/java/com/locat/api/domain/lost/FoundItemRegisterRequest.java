package com.locat.api.domain.lost;

public record FoundItemRegisterRequest(
    Long categoryId,
    Long colorId,
    String itemName,
    String description,
    String custodyLocation) {}
