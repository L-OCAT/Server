package com.locat.api.domain.common.dto;

/**
 * 기본 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record BaseResponse<T>(String message, T data) {

  public static <T> BaseResponse<T> empty() {
    return new BaseResponse<>("OK", null);
  }

  public static <T> BaseResponse<T> of(final T data) {
    return new BaseResponse<>("OK", data);
  }
}
