package com.locat.api.domain.geo.found;

public record FoundItemRegisterRequest(
    Long categoryId, Long colorId, String itemName, String description, String custodyLocation) {}
