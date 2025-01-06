package com.locat.api.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

public final class TypeCaster {

  private static final Map<Class<?>, Function<String, ?>> PARSERS =
      Map.of(
          String.class,
          Function.identity(),
          Integer.class,
          Integer::parseInt,
          Long.class,
          Long::parseLong,
          Double.class,
          Double::parseDouble,
          Float.class,
          Float::parseFloat,
          Boolean.class,
          Boolean::parseBoolean,
          LocalDate.class,
          LocalDate::parse,
          LocalDateTime.class,
          LocalDateTime::parse);

  private TypeCaster() {
    // Utility class
  }

  /**
   * 주어진 문자열 값을 주어진 타입으로 변환합니다.
   *
   * @param value 문자열 값
   * @param clazz 변환할 타입 클래스
   * @return 변환된 값
   * @param <T> 변환할 타입
   * @throws IllegalArgumentException 지원하지 않는 타입인 경우
   * @apiNote 지원하는 타입: {@link String}, {@link Integer}, {@link Long}, {@link Double}, {@link Float},
   *     {@link Boolean}, {@link LocalDate}, {@link LocalDateTime}
   */
  public static <T> T cast(String value, Class<T> clazz) {
    Function<String, ?> parser = PARSERS.get(clazz);
    if (parser == null) {
      throw new IllegalArgumentException(
          String.format("Cast to %s is not supported", clazz.getSimpleName()));
    }
    return clazz.cast(parser.apply(value));
  }
}
