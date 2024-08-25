package com.locat.api.global.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomGenerator {

  private RandomGenerator() {
    // Utility class
  }

  private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final Random RANDOM = new SecureRandom();

  /**
   * 주어진 길이의 무작위 코드를 생성합니다. <br>
   * 기준 문자열: {@code ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789}
   *
   * @param requiredLength 생성할 코드의 길이
   * @return 생성된 코드
   */
  public static String generateRandomCode(final int requiredLength) {
    StringBuilder sb = new StringBuilder(requiredLength);
    for (int i = 0; i < requiredLength; i++) {
      sb.append(CHARACTERS_FOR_CODE.charAt(RANDOM.nextInt(CHARACTERS_FOR_CODE.length())));
    }
    return sb.toString();
  }

  /**
   * 주어진 길이의 무작위 바이트 배열을 생성합니다.
   *
   * @param requiredLength 생성할 바이트 배열의 길이
   * @return 생성된 바이트 배열
   */
  public static byte[] generateRandomBytes(final int requiredLength) {
    byte[] bytes = new byte[requiredLength];
    RANDOM.nextBytes(bytes);
    return bytes;
  }
}
