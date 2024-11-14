package com.locat.api.domain.geo.base.entity;

import com.locat.api.global.exception.custom.InvalidParameterException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeoItemType {
  LOSTS("losts"),
  FOUNDS("founds");

  private final String value;

  public static GeoItemType fromValue(String value) {
    return Arrays.stream(GeoItemType.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElseThrow(
            () -> new InvalidParameterException("No matching GeoItemType found. value: " + value));
  }
}
