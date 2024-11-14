package com.locat.api.global.utils;

import com.locat.api.global.exception.custom.InternalProcessingException;
import jakarta.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class HashingUtils {

  private static final String HASH_ALGORITHM = "SHA-256";

  private HashingUtils() {
    // Utility class
  }

  /**
   * 주어진 값을 해싱하여 반환합니다.
   *
   * @param value 해싱할 값 (never {@code null})
   * @return 해싱된 값
   */
  public static String hash(@NotNull final String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
      byte[] hash = digest.digest(value.getBytes());
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new InternalProcessingException("Can't process hash. / Reason: " + e.getMessage());
    }
  }
}
