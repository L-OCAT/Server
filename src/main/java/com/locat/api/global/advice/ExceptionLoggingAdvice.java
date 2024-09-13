package com.locat.api.global.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "exceptionLogger")
public class ExceptionLoggingAdvice {

  @AfterThrowing(pointcut = "execution(* com.locat.api..*.*(..))", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    this.doLogExceptionInternal(joinPoint, ex);
  }

  private void doLogExceptionInternal(JoinPoint joinPoint, Throwable ex) {
    StackTraceElement invokedMethod = ex.getStackTrace()[0];
    log.error(
        "Resolved exception[{}] | Location: {} | Method: {}:{} | Message: {}",
        ex.getClass().getSimpleName(),
        joinPoint.getSignature().toShortString(),
        invokedMethod.getMethodName(),
        invokedMethod.getLineNumber(),
        ex.getMessage());
  }
}
