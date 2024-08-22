package com.locat.api.domain.geo.base.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColorType {
  WHITE("#FFFFFF"),
  BLACK("#000000"),
  GREY("#E5E5E5"),
  RED("#FF0000"),
  ORANGE("#FFA500"),
  YELLOW("#FFFF00"),
  GREEN("#008000"),
  SKY_BLUE("#87CEEB"),
  BLUE("#0000FF"),
  PURPLE("#800080"),
  PINK("#FFC0CB"),
  BROWN("#A52A2A"),
  ETC(null);

  private final String hexCode;

  public static ColorType fromHexCode(String hexCode) {
    return Arrays.stream(ColorType.values())
        .filter(color -> color.getHexCode().equals(hexCode))
        .findFirst()
        .orElse(ETC);
  }
}
