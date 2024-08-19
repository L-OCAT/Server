package com.locat.api.global.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomCodeGenerator {

  private RandomCodeGenerator() {
    // Utility class
  }

  private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final Random RANDOM = new SecureRandom();

  /**
   * 주어진 길이의, 무작위 코드를 생성합니다. <br>
   * 기준 문자열: {@code ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789}
   *
   * @param requiredLength 생성할 코드의 길이
   */
  public static String generate(final int requiredLength) {
    StringBuilder sb = new StringBuilder(requiredLength);
    for (int i = 0; i < requiredLength; i++) {
      sb.append(CHARACTERS_FOR_CODE.charAt(RANDOM.nextInt(CHARACTERS_FOR_CODE.length())));
    }
    return sb.toString();
  }
}
