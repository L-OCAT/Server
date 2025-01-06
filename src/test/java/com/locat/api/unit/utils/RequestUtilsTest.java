package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestUtilsTest {

  @Test
  @DisplayName("파라미터가 존재하면, 값을 반환해야 한다")
  void whenParameterExists_thenReturnParameterValue() {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("testParam")).thenReturn("123");

    // When
    Integer result = RequestUtils.getParameterOrDefault(request, "testParam", Integer.class, null);

    // Then
    assertThat(result).isEqualTo(123);
  }

  @Test
  @DisplayName("파라미터가 존재하지 않으면, 기본값을 반환해야 한다")
  void whenParameterDoesNotExist_thenReturnDefaultValue() {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("testParam")).thenReturn(null);

    // When
    String result =
        RequestUtils.getParameterOrDefault(request, "testParam", String.class, "default");

    // Then
    assertThat(result).isEqualTo("default");
  }

  @Test
  @DisplayName("파라미터가 없고 기본값도 null이면, 예외가 발생해야 한다")
  void whenParameterDoesNotExistAndNoDefault_thenThrowException() {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("testParam")).thenReturn(null);

    // When / Then
    assertThatThrownBy(
            () -> RequestUtils.getParameterOrDefault(request, "testParam", String.class, null))
        .isExactlyInstanceOf(InvalidParameterException.class);
  }

  @Test
  @DisplayName("파라미터 값을 지정된 타입으로 변환할 수 없으면, 예외가 발생해야 한다")
  void whenParameterCannotBeCast_thenThrowException() {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("testParam")).thenReturn("Hello");

    // When / Then
    assertThatThrownBy(
            () -> RequestUtils.getParameterOrDefault(request, "testParam", Integer.class, null))
        .isInstanceOf(Exception.class);
  }
}
