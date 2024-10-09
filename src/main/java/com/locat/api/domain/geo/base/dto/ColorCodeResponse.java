package com.locat.api.domain.geo.base.dto;

import com.locat.api.domain.geo.base.entity.ColorCode;

public record ColorCodeResponse(Long id, String hexCode, String name) {

  public static ColorCodeResponse toResponse(ColorCode colorCode) {
    return new ColorCodeResponse(colorCode.getId(), colorCode.getHexCode(), colorCode.getName());
  }
}
