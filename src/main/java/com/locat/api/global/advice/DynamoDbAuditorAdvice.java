package com.locat.api.global.advice;

import com.locat.api.global.annotations.CreatedBy;
import com.locat.api.global.annotations.CreatedDate;
import com.locat.api.global.annotations.LastModifiedBy;
import com.locat.api.global.annotations.LastModifiedDate;
import com.locat.api.global.exception.InternalProcessingException;
import com.locat.api.global.security.LocatAuditorAware;
import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamoDbAuditorAdvice {

  private final Clock clock;
  private final LocatAuditorAware locatAuditorAware;

  @Before("execution(* com.locat.api..*DynamoDbCrudRepository+.save(..))")
  public void setAuditingInfoBeforeSave(JoinPoint joinPoint) {
    Object arg = joinPoint.getArgs()[0];
    if (this.isDynamoDbBean(arg)) {
      this.setAuditFields(arg, true);
    }
  }

  @Before("execution(* com.locat.api..*DynamoDbCrudRepository+.update(..))")
  public void setAuditingInfoBeforeUpdate(JoinPoint joinPoint) {
    Object arg = joinPoint.getArgs()[0];
    if (this.isDynamoDbBean(arg)) {
      this.setAuditFields(arg, false);
    }
  }

  private boolean isDynamoDbBean(Object arg) {
    return arg.getClass().isAnnotationPresent(DynamoDbBean.class);
  }

  private void setAuditFields(Object arg, boolean isNew) {
    ZonedDateTime now = ZonedDateTime.now(clock);
    Long currentAuditor = this.getCurrentAuditor();

    for (Field field : arg.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(CreatedDate.class) && isNew) {
        this.setFieldValue(arg, field, now);
      } else if (field.isAnnotationPresent(CreatedBy.class) && isNew) {
        this.setFieldValue(arg, field, currentAuditor);
      } else if (field.isAnnotationPresent(LastModifiedDate.class)) {
        this.setFieldValue(arg, field, now);
      } else if (field.isAnnotationPresent(LastModifiedBy.class)) {
        this.setFieldValue(arg, field, currentAuditor);
      }
    }
  }

  private Long getCurrentAuditor() {
    return locatAuditorAware
        .getCurrentAuditor()
        .orElseThrow(
            () ->
                new InternalProcessingException(
                    "Cannot get current auditor from SecurityContext."));
  }

  private void setFieldValue(Object arg, Field field, Object value) {
    try {
      field.setAccessible(true);
      Object convertedValue = convertValueToFieldType(field.getType(), value);
      if (this.shouldUpdateField(field, convertedValue)) {
        field.set(arg, convertedValue);
      }
    } catch (IllegalAccessException e) {
      throw new InternalProcessingException("Error setting field value: " + field.getName());
    }
  }

  private boolean shouldUpdateField(Field field, Object value) {
    try {
      return field.get(value) == null || value != null;
    } catch (IllegalAccessException e) {
      throw new InternalProcessingException("Error checking field value: " + field.getName());
    }
  }

  private Object convertValueToFieldType(Class<?> fieldType, Object value) {
    if (value instanceof ZonedDateTime zonedDateTime) {
      if (fieldType.equals(ZonedDateTime.class)) {
        return zonedDateTime;
      } else if (fieldType.equals(LocalDateTime.class)) {
        return zonedDateTime.toLocalDateTime();
      } else if (fieldType.equals(Instant.class)) {
        return zonedDateTime.toInstant();
      } else if (fieldType.equals(Date.class)) {
        return Date.from(zonedDateTime.toInstant());
      } else if (fieldType.equals(Long.class)) {
        return zonedDateTime.toInstant().toEpochMilli();
      }
    }
    return value;
  }
}
