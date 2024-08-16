package com.locat.api.domain.core;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

import static com.locat.api.global.exception.ApiExceptionType.*;

/**
 * Standard Error Response
 *
 * @param message HTTP Status Message(e.g., "Not Found", "Bad Request")
 * @param data 자세한 예외 정보
 * @see ApiExceptionType
 */
public record ErrorResponse(String message, ErrorData data) {

  public static ErrorResponse fromException(LocatApiException ex) {
    return new ErrorResponse(
        ex.getHttpStatus().getReasonPhrase(), ErrorData.of(ex.getMessage(), ex.getCode()));
  }

  public static ErrorResponse forbidden() {
    return new ErrorResponse(
        FORBIDDEN.getMessage(), ErrorData.of("접근 권한이 없습니다.", FORBIDDEN.getCode()));
  }

  public static ErrorResponse unauthorized() {
    return new ErrorResponse(
        UNAUTHORIZED.getMessage(), ErrorData.of("인증 수단이 없거나, 유효하지 않습니다.", UNAUTHORIZED.getCode()));
  }

  public static ErrorResponse badRequest(String message) {
    return new ErrorResponse(
        BAD_REQUEST.getMessage(), ErrorData.of(message, BAD_REQUEST.getCode()));
  }

  public static ErrorResponse methodNotAllowed(String message) {
    return new ErrorResponse(
        NOT_ALLOWED_METHOD.getMessage(), ErrorData.of(message, NOT_ALLOWED_METHOD.getCode()));
  }

  public static ErrorResponse internalServerError() {
    return new ErrorResponse(
        INTERNAL_SERVER_ERROR.getMessage(),
        ErrorData.of("서버 내부 오류가 발생했습니다.", INTERNAL_SERVER_ERROR.getCode()));
  }
}
