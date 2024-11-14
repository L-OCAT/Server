package com.locat.api.global.web.validation;

import com.locat.api.domain.geo.base.validation.FoundItemRegisterRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/** 습득물 등록 시, 요청 값의 유효성을 검사하도록 지정하는 어노테이션 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FoundItemRegisterRequestValidator.class)
public @interface FoundItemValidation {

  String message() default "습득물 등록을 위한 정보가 올바르지 않습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
