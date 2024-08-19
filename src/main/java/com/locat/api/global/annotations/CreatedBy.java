package com.locat.api.global.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, METHOD, ANNOTATION_TYPE})
public @interface CreatedBy {}
