package com.locat.api.domain.user.entity;

public enum PlatformType {
    IOS("ios"),
    ANDROID("android");

    private final String value;

    PlatformType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
