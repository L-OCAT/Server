package com.locat.api.global.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RequestLoggingAdvice {

  @Before("execution(* com..*.controller.*.*(..))")
  public void debugLogBeforeRequest() {
    this.doLogCurrentRequest();
  }

  private void doLogCurrentRequest() {
    if (log.isDebugEnabled()) {
      HttpServletRequest currentRequest = this.getCurrentHttpRequest();
      log.debug(
          """
					[RequestLog]
					Request: {} {}
					User-Agent: {}
					From: {}
					""",
          currentRequest.getMethod(),
          currentRequest.getRequestURI(),
          currentRequest.getHeader(HttpHeaders.USER_AGENT),
          currentRequest.getRemoteAddr());
    }
  }

  private HttpServletRequest getCurrentHttpRequest() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest();
  }
}
