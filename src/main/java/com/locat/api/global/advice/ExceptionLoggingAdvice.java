package com.locat.api.global.advice;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAdvice {

  @AfterThrowing(pointcut = "execution(* com.locat.api..*.*(..))", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    this.doLogExceptionInternal(joinPoint, ex);
  }

  private void doLogExceptionInternal(JoinPoint joinPoint, Throwable ex) {
    Signature signature = joinPoint.getSignature();
    log.error(
        "[Exception Log] Resolved exception[{}] | Message: {} | Method: {}:{} (Location: {})",
        ex.getClass().getSimpleName(),
        signature.getDeclaringTypeName(),
        signature.getName(),
        signature.toShortString(),
        ex.getMessage());
    if (log.isDebugEnabled()) {
      Optional.of(ex)
          .map(Throwable::getCause)
          .ifPresent(
              cause ->
                  log.debug(
                      "> Caused by [{}] | Message: {}",
                      cause.getClass().getSimpleName(),
                      cause.getMessage()));
    }
  }
}
