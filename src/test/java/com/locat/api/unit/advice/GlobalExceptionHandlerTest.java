package com.locat.api.unit.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.locat.api.domain.auth.exception.EmailAlreadySentException;
import com.locat.api.domain.common.dto.ErrorResponse;
import com.locat.api.global.exception.GlobalExceptionHandler;
import com.locat.api.global.exception.LocatApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  @DisplayName("LocatApiException 처리하여 지정된 상태 코드와 오류 응답을 반환한다.")
  void handleLocatApiException() {
    // Given
    LocatApiException exception = mock(LocatApiException.class);
    when(exception.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleLocatApiException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(ErrorResponse.fromException(exception));
  }

  @Test
  @DisplayName("EmailAlreadySentException 처리하여 상태 코드와 Retry-After 헤더를 반환한다.")
  void handleEmailAlreadySentException() {
    // Given
    EmailAlreadySentException exception = mock(EmailAlreadySentException.class);
    when(exception.getHttpStatus()).thenReturn(HttpStatus.TOO_MANY_REQUESTS);
    when(exception.getRetryAfter()).thenReturn(30L);

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleEmailAlreadySentException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    assertThat(response.getHeaders().getFirst(HttpHeaders.RETRY_AFTER)).isEqualTo("30");
    assertThat(response.getBody()).isEqualTo(ErrorResponse.fromException(exception));
  }

  @Test
  @DisplayName("API가 지원하지 않는 HTTP Method로 요청하면, 405 상태 코드와 허용된 메서드 목록을 반환한다.")
  void handleMethodNotSupportedException() {
    // Given
    HttpRequestMethodNotSupportedException exception =
        new HttpRequestMethodNotSupportedException("POST", List.of("GET", "PUT"));

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleMethodNotSupportedException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    assertThat(response.getHeaders().getFirst(HttpHeaders.ALLOW)).isEqualTo("[GET, PUT]");
    assertThat(response.getBody().message()).contains("Method Not Allowed");
    assertThat(response.getBody().data().message()).contains("POST", "GET", "PUT");
  }

  @Test
  @DisplayName("요청 Parameter가 유효하지 않으면, 400 상태 코드를 반환한다.")
  void handleArgumentNotValidException() {
    // Given
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock(BindingResult.class);
    when(exception.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors())
        .thenReturn(List.of(new FieldError("object", "field", "error message")));

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleArgumentNotValidException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().message()).contains("Bad Request");
  }

  @Test
  @DisplayName("ConstraintViolation가 발생하면, 400 상태 코드를 반환한다.")
  void handleConstraintViolationException() {
    // Given
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getPropertyPath().toString()).thenReturn("property");
    when(violation.getMessage()).thenReturn("must not be null");
    ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleConstraintViolationException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().message()).contains("Bad Request");
    assertThat(response.getBody().data().message()).contains("property - must not be null.");
  }

  @Test
  @DisplayName("API URI를 찾을 수 없다면, 404 상태 코드를 반환한다.")
  void handleNoHandlerFoundException() {
    // Given
    NoHandlerFoundException exception = mock(NoHandlerFoundException.class);

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleNoHandlerFoundException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().message()).isEqualTo("Not Found");
  }

  @Test
  @DisplayName("그 외 모든 예외는, 500 내부 서버 오류를 반환한다.")
  void handleAllUncaughtException() {
    // Given
    Exception exception = new Exception();

    // When
    ResponseEntity<ErrorResponse> response =
        ReflectionTestUtils.invokeMethod(
            this.exceptionHandler, "handleAllUncaughtException", exception);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isEqualTo(ErrorResponse.internalServerError());
  }
}
