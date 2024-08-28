package com.locat.api.global.utils;

import com.locat.api.global.exception.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/** {@link HttpServletRequest} 관련 유틸리티 클래스 */
public final class RequestUtils {

  private RequestUtils() {
    // Utility class
  }

  /**
   * HTTP 요청에서 주어진 이름의 파라미터 값을 반환합니다. <br>
   * 파라미터가 존재하지 않으면 기본값을 반환합니다. <br>
   * 파라미터가 존재하지 않고 기본값도 null이면 예외가 발생합니다.
   *
   * @param <T> 반환할 값의 타입
   * @param request HTTP 요청 객체
   * @param parameterName 요청 파라미터 이름 (never {@code null})
   * @param clazz 반환할 값의 타입 클래스 (never {@code null})
   * @param defaultValue 파라미터가 없을 경우 사용할 기본값 or {@code null}
   * @return 요청 파라미터 값 또는 기본값
   * @throws InvalidParameterException 필수 파라미터가 없고 기본값이 null인 경우
   * @apiNote 기본값 없이 사용자로부터 반드시 입력을 받아야 하는 경우 defaultValue를 null로 설정
   */
  public static <T> T getParameterOrDefault(
      HttpServletRequest request, String parameterName, Class<T> clazz, T defaultValue) {
    return Optional.of(request)
        .map(req -> req.getParameter(parameterName))
        .map(value -> TypeCaster.cast(value, clazz))
        .orElseGet(
            () -> {
              if (defaultValue != null) {
                return defaultValue;
              }
              throw new InvalidParameterException(
                  String.format("Required parameter \"%s\" is missing", parameterName));
            });
  }
}
