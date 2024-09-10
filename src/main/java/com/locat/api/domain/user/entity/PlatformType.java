package com.locat.api.domain.user.entity;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.notification.NotificationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
