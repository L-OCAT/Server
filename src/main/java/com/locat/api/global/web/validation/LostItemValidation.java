package com.locat.api.global.web.validation;

import com.locat.api.domain.geo.base.validation.LostItemRegisterRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** 분실물 등록 시, 요청 값의 유효성을 검사하도록 지정하는 어노테이션 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LostItemRegisterRequestValidator.class)
public @interface LostItemValidation {

  String message() default "분실물 등록을 위한 정보가 올바르지 않습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
