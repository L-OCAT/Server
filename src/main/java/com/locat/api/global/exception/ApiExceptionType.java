package com.locat.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {
  BAD_REQUEST(400, "Bad Request", 400000),
  INVALID_PARAMETER(400, "Bad Request: Required Parameters are Invalid or Missing.", 400001),
  FILE_EXTENSION_NOT_PROVIDED(400, "Bad Request: File Extension Not Provided", 400002),
  FILE_SIZE_LIMIT_EXCEEDED(400, "Bad Request: File Size Limit Exceeded", 400003),
  FILE_EXTENSION_NOT_SUPPORTED(400, "Bad Request: File Extension Not Supported", 400004),
  INVALID_EMAIL_VERIFICATION_CODE(
      400, "Bad Request: Provided Verification Code is Not Matched", 400005),
  INVALID_PLATFORM(400, "Bad Request: Platform Type is Invalid", 400006),

  UNAUTHORIZED(401, "Unauthorized", 401000),
  INVALID_TOKEN(401, "Unauthorized: Invalid JWT", 401001),
  INVALID_REFRESH_TOKEN(
      401, "Unauthorized: Invalid Refresh Token (Expired or Not Matched)", 401002),

  FORBIDDEN(403, "Forbidden", 403000),

  NOT_FOUND(404, "Not Found", 404000),
  NOT_FOUND_USER(404, "Not Found: User", 404001),
  NOT_FOUND_FILE(404, "Not Found: File", 404002),
  NOT_FOUND_AUTH(404, "Not Found: Authorization Method", 404003),
  NOT_FOUND_ITEM_FOUND(404, "Not Found: Found Item", 404004),
  NOT_FOUND_ITEM_LOST(404, "Not Found: Lost Item", 404005),
  NOT_FOUND_CATEGORY(404, "Not Found: Category", 404006),
  NOT_FOUND_COLOR_CODE(404, "Not Found: Color Code", 404007),
  NOT_FOUND_ENDPOINT(404, "Not Found: Endpoint ARN Not Found", 404008),

  NOT_ALLOWED_METHOD(405, "Method Not Allowed", 405000),

  CONFLICT(409, "Conflict", 409000),

  TOO_MANY_REQUESTS(429, "Too Many Requests", 429000),
  VERIFICATION_EMAIL_ALREADY_SENT(429, "Email Verification Code Already Sent", 429001),

  INTERNAL_SERVER_ERROR(500, "Internal Server Error", 500000),
  FAIL_TO_CONSTRUCT_EMAIL(500, "Failed to Construct Mail", 500001),
  FAIL_TO_SEND_EMAIL(500, "Failed to Send Mail", 500002),
  S3_ERROR(500, "Something went Wrong with S3", 500003),
  FAIL_TO_READ_FILES(500, "Failed to Read Files", 500004),
  CANNOT_PROCESS_JWT_NO_MATCHING_ALGORITHM(
      500, "Cannot Process JWT: No Matching Algorithm", 500005),
  FAIL_TO_SEND_PUSH_NOTIFICATION(500, "Failed to Send Push Notification", 500006),
  FAIL_TO_CREATE_ENDPOINT(500, "Failed to Create Platform Endpoint", 500007),
  FAIL_TO_SUBSCRIBE_TOPIC(500, "Failed to Subscribe Endpoint to Topic", 500008);

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
