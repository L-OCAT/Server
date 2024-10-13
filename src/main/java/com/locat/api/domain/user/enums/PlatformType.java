package com.locat.api.domain.user.enums;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.notification.NotificationException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {
  IOS("ios"),
  ANDROID("android");

  private final String value;

  public static PlatformType fromValue(final String value) {
    return Arrays.stream(PlatformType.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElseThrow(() -> new NotificationException(ApiExceptionType.INVALID_PLATFORM));
  }
}
