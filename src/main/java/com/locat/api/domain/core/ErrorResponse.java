package com.locat.api.domain.core;

import static com.locat.api.global.exception.ApiExceptionType.*;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.LocatApiException;

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
        FORBIDDEN.getMessage(), ErrorData.of("No permission.", FORBIDDEN.getCode()));
  }

  public static ErrorResponse unauthorized() {
    return new ErrorResponse(
        UNAUTHORIZED.getMessage(),
        ErrorData.of("Authentication method is NOT provided or Invalid.", UNAUTHORIZED.getCode()));
  }

  public static ErrorResponse badRequest(String message) {
    return new ErrorResponse(
        BAD_REQUEST.getMessage(), ErrorData.of(message, BAD_REQUEST.getCode()));
  }

  public static ErrorResponse notFound(String message) {
    return new ErrorResponse(NOT_FOUND.getMessage(), ErrorData.of(message, NOT_FOUND.getCode()));
  }

  public static ErrorResponse methodNotAllowed(String message) {
    return new ErrorResponse(
        NOT_ALLOWED_METHOD.getMessage(), ErrorData.of(message, NOT_ALLOWED_METHOD.getCode()));
  }

  public static ErrorResponse internalServerError() {
    return new ErrorResponse(
        INTERNAL_SERVER_ERROR.getMessage(),
        ErrorData.of(
            "Something went wrong. Please try again later.", INTERNAL_SERVER_ERROR.getCode()));
  }
}
