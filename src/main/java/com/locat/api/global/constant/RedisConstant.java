package com.locat.api.global.constant;

public final class RedisConstant {

  private RedisConstant() {
    // 유틸리티 클래스는 인스턴스화할 수 없습니다.
  }

  public static final String EMAIL_VERIFICATION_CODE = "EMAIL_VERIFICATION_CODE::";

  public static final String BLACKLISTED_TOKEN = "BLACKLISTED::";

  public static final String WHITELISTED_EMAIL = "WHITELISTED::";
}
