package com.locat.api.global.annotation;

import com.locat.api.domain.user.enums.UserType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminApi {

  /** 이 API를 {@link UserType#SUPER_ADMIN}만 사용할 수 있도록 설정합니다. */
  boolean superAdminOnly() default false;

  /** 이 API 요청 시 감사 로그(logback & DB)를 남길지 여부를 설정합니다. */
  boolean audit() default false;
}
