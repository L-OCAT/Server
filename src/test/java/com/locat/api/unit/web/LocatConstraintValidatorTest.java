package com.locat.api.unit.web;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.locat.api.global.web.validation.LocatConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

final class LocatConstraintValidatorTest {

  static class TestValidator extends LocatConstraintValidator<Annotation, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
      return true;
    }
  }

  @Test
  @DisplayName("setCustomViolationMessage 메서드로 사용자 정의 Violation 메세지를 설정한다.")
  void testSetCustomMessage() {
    // Given
    ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
    ConstraintValidatorContext.ConstraintViolationBuilder mockBuilder =
        mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    given(mockContext.buildConstraintViolationWithTemplate(anyString())).willReturn(mockBuilder);
    given(mockBuilder.addPropertyNode(anyString()))
        .willReturn(
            mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext
                    .class));

    TestValidator validator = new TestValidator();
    String customMessage = "This is a custom message.";
    String fieldName = "fieldName";

    // When & Then
    assertThatCode(
            () ->
                ReflectionTestUtils.invokeMethod(
                    validator, "setCustomViolationMessage", mockContext, customMessage, fieldName))
        .doesNotThrowAnyException();
    then(mockContext).should().disableDefaultConstraintViolation();
    then(mockContext).should().buildConstraintViolationWithTemplate(customMessage);
  }
}
