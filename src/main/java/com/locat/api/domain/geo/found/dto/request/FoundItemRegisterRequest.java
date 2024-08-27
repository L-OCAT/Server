package com.locat.api.domain.geo.found.dto.request;

import com.locat.api.domain.geo.base.annotation.FoundItemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 습득물 등록 요청 DTO
 *
 * @param categoryId 카테고리 ID (never {@code null})
 * @param colorId 색상 ID (never {@code null})
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param lat 습득 장소 위도
 * @param lng 습득 장소 경도
 */
@FoundItemValidation
public record FoundItemRegisterRequest(
    @Positive Long categoryId,
    @Positive Long colorId,
    @NotBlank String itemName,
    @NotBlank String description,
    @NotBlank String custodyLocation,
    Double lat,
    Double lng) {}
