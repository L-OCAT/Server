package com.locat.api.domain.geo.lost.dto;

public record LosttemRegisterRequest(
    Long categoryId,
    Long colorId,
    String itemName,
    String description,
    Boolean isWillingToPayGratuity,
    Double latitude,
    Double longitude) {}
