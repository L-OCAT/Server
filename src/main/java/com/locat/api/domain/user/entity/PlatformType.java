package com.locat.api.domain.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {
    IOS("ios"),
    ANDROID("android");

    private final String value;
}
