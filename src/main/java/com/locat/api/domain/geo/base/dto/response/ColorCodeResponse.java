package com.locat.api.domain.geo.base.dto.response;

import com.locat.api.domain.geo.base.entity.ColorCode;

/**
 * 색상 코드 응답 DTO
 *
 * @param id 색상 코드 ID
 * @param hexCode 색상 코드
 * @param name 색상 코드 이름
 */
public record ColorCodeResponse(Long id, String hexCode, String name) {

  public static ColorCodeResponse toResponse(ColorCode colorCode) {
    return new ColorCodeResponse(colorCode.getId(), colorCode.getHexCode(), colorCode.getName());
  }
}
