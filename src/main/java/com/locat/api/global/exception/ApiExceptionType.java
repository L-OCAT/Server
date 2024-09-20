package com.locat.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {
  BAD_REQUEST(400, "Bad Request", 400000),
  INVALID_PARAMETER(400, "Bad Request: Required parameters are invalid or missing.", 400001),
  FILE_EXTENSION_NOT_PROVIDED(400, "Bad Request: File extension NOT provided", 400002),
  FILE_SIZE_LIMIT_EXCEEDED(400, "Bad Request: File size limit exceeded", 400003),
  FILE_EXTENSION_NOT_SUPPORTED(400, "Bad Request: File extension not supported", 400004),
  INVALID_EMAIL_VERIFICATION_CODE(
      400, "Bad Request: Verification code is not matched", 400005),
  INVALID_PLATFORM(400, "Bad Request: Invalid platform type", 400006),

  UNAUTHORIZED(401, "Unauthorized", 401000),
  INVALID_TOKEN(401, "Unauthorized: Invalid JWT (expired or not matched)", 401001),

  FORBIDDEN(403, "Forbidden", 403000),

  NOT_FOUND(404, "Not Found", 404000),
  NOT_FOUND_USER(404, "Not Found: User", 404001),
  NOT_FOUND_FILE(404, "Not Found: File", 404002),
  NOT_FOUND_AUTH(404, "Not Found: Authorization Method", 404003),
  NOT_FOUND_ITEM_FOUND(404, "Not Found: Found Item", 404004),
  NOT_FOUND_ITEM_LOST(404, "Not Found: Lost Item", 404005),
  NOT_FOUND_CATEGORY(404, "Not Found: Category", 404006),
  NOT_FOUND_COLOR_CODE(404, "Not Found: Color Code", 404007),
  NOT_FOUND_ENDPOINT(404, "Not Found: Endpoint ARN", 404008),
  NOT_FOUND_TERMS(404, "Not Found: Terms", 404009),

  NOT_ALLOWED_METHOD(405, "Method Not Allowed", 405000),

  CONFLICT(409, "Conflict", 409000),
  RESOURCE_ALREADY_EXISTS(409, "Conflict: The requested resource already exists", 409001),
  RESOURCE_IDENTICAL(409, "Conflict: The requested resource is identical", 409002),

  TOO_MANY_REQUESTS(429, "Too Many Requests", 429000),
  VERIFICATION_EMAIL_ALREADY_SENT(429, "Code already sent", 429001),

  INTERNAL_SERVER_ERROR(500, "Internal Server Error", 500000),
  FAIL_TO_CONSTRUCT_EMAIL(500, "Failed to construct mail", 500001),
  FAIL_TO_SEND_EMAIL(500, "Failed to send mail", 500002),
  S3_ERROR(500, "Something went wrong with S3", 500003),
  FAIL_TO_READ_FILES(500, "Failed to read files", 500004),
  FAIL_TO_SEND_PUSH_NOTIFICATION(500, "Failed to send push notification", 500005),
  FAIL_TO_CREATE_ENDPOINT(500, "Failed to create platform endpoint", 500006),
  FAIL_TO_SUBSCRIBE_TOPIC(500, "Failed to subscribe endpoint to topic", 500007);

  /**
   * 각 예외 상황에 대한 적절한 HTTP Status Code
   *
   * @see HttpStatus
   */
  private final int statusCode;

  /** 클라이언트에게 전달할 예외 상황에 대한 정보 또는 메세지 */
  private final String message;

  /** 디버그, 내부 관리를 위한 에러 코드 */
  private final int code;
}
