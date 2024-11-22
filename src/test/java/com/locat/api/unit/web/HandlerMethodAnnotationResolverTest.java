package com.locat.api.unit.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.locat.api.global.web.resolver.HandlerMethodAnnotationResolver;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

class HandlerMethodAnnotationResolverTest {

  @InjectMocks private HandlerMethodAnnotationResolver annotationResolver;

  @Mock private RequestMappingHandlerMapping handlerMapping;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("메서드 레벨에서 어노테이션을 찾아 반환해야 한다.")
  void shouldReturnAnnotationFromHandlerMethod() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    TestController controller = new TestController();
    HandlerMethod handlerMethod =
        new HandlerMethod(controller, TestController.class.getDeclaredMethod("annotatedMethod"));
    HandlerExecutionChain chain = mock(HandlerExecutionChain.class);
    given(this.handlerMapping.getHandler(any(HttpServletRequest.class))).willReturn(chain);
    given(chain.getHandler()).willReturn(handlerMethod);

    // When
    Optional<TestAnnotation> result = this.annotationResolver.find(request, TestAnnotation.class);

    // Then
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("클래스 레벨에서 어노테이션을 찾아 반환해야 한다.")
  void shouldReturnAnnotationFromClass() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    TestClassWithAnnotation controller = new TestClassWithAnnotation();
    HandlerMethod handlerMethod =
        new HandlerMethod(
            controller, TestClassWithAnnotation.class.getDeclaredMethod("nonAnnotatedMethod"));
    HandlerExecutionChain chain = mock(HandlerExecutionChain.class);

    given(chain.getHandler()).willReturn(handlerMethod);
    given(this.handlerMapping.getHandler(any(HttpServletRequest.class))).willReturn(chain);

    // When
    Optional<TestAnnotation> result = this.annotationResolver.find(request, TestAnnotation.class);

    // Then
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("HandlerMethod를 찾는데 실패했다면, 빈 Optional을 반환해야 한다.")
  void shouldReturnEmptyWhenExceptionOccurs() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    given(this.handlerMapping.getHandler(any(HttpServletRequest.class)))
        .willThrow(new RuntimeException("Exception!"));

    // When
    Optional<TestAnnotation> result = this.annotationResolver.find(request, TestAnnotation.class);

    // Then
    assertThat(result).isEmpty();
  }

  static class TestController {
    @TestAnnotation
    public void annotatedMethod() {}
  }

  @TestAnnotation
  static class TestClassWithAnnotation {
    public void nonAnnotatedMethod() {}
  }

  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface TestAnnotation {}
}
