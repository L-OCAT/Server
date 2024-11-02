package com.locat.api.global.web;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
@Slf4j
@RequiredArgsConstructor
public class HandlerMethodAnnotationResolver {

  private final RequestMappingHandlerMapping requestMappingHandlerMapping;

  /**
   * HTTP 요청에서 {@link HandlerMethod}를 찾고, 해당 핸들러 메서드에서 지정된 어노테이션을 찾아 반환합니다. <br>
   * 메서드 > 클래스 레벨의 순서로 어노테이션을 찾습니다.
   *
   * @param request HTTP 요청
   * @param targetAnnotation 찾을 어노테이션
   * @return 찾은 어노테이션, 없을 경우 {@link Optional#empty()}
   * @param <A> 찾을 어노테이션의 타입
   */
  public <A extends Annotation> Optional<A> find(
      HttpServletRequest request, Class<A> targetAnnotation) {
    return this.resolve(request)
        .flatMap(handlerMethod -> this.findAnnotation(handlerMethod, targetAnnotation));
  }

  private Optional<HandlerMethod> resolve(HttpServletRequest request) {
    try {
      HandlerExecutionChain handler = this.requestMappingHandlerMapping.getHandler(request);
      if (handler != null && handler.getHandler() instanceof HandlerMethod handlerMethod) {
        return Optional.of(handlerMethod);
      }
    } catch (Exception e) {
      if (log.isDebugEnabled()) {
        log.debug("Failed to resolve handler method for request: {}", request.getRequestURI(), e);
      }
      return Optional.empty();
    }
    return Optional.empty();
  }

  private <A extends Annotation> Optional<A> findAnnotation(
      HandlerMethod handlerMethod, Class<A> targetAnnotation) {
    A annotation = handlerMethod.getMethodAnnotation(targetAnnotation);
    if (annotation != null) {
      return Optional.of(annotation);
    }
    // 메서드 레벨에서 찾지 못한 경우 클래스 레벨에서 가져오기 시도
    return Optional.ofNullable(handlerMethod.getBeanType().getAnnotation(targetAnnotation));
  }
}
