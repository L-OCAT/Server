package com.locat.api.domain.common.dto;

/**
 * 예외 발생 시 클라이언트에게 전달할 메세지, 코드 정보 DTO
 *
 * @param message 메세지
 * @param code 코드
 */
public record ErrorData(String message, Integer code) {

  public static ErrorData of(String message, Integer code) {
    return new ErrorData(message, code);
  }
}
