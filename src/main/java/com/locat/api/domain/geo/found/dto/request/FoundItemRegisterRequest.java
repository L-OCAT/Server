package com.locat.api.domain.geo.found.dto.request;

import com.locat.api.domain.geo.base.annotation.FoundItemValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 습득물 등록 요청 DTO
 *
 * @param categoryId 카테고리 ID (직접 입력한 카테고리일 경우 null)
 * @param categoryName 카테고리 이름 (직접 입력한 카테고리일 경우 필수)
 * @param colorHexCode 색상 HEX 코드
 * @param itemName 습득물 이름
 * @param description 습득물 설명
 * @param custodyLocation 보관 장소
 * @param latitude 습득 장소 위도
 * @param longitude 습득 장소 경도
 */
@FoundItemValidation
public record FoundItemRegisterRequest(
    @Positive Long categoryId,
    String categoryName,
    @NotBlank String colorHexCode,
    @NotBlank String itemName,
    @NotBlank String description,
    @NotBlank String custodyLocation,
    Double latitude,
    Double longitude) {}
