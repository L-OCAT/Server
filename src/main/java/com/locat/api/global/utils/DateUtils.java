package com.locat.api.global.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public final class DateUtils {

  private DateUtils() {}

  private static final DateTimeFormatter MONTH_FORMATTER =
      DateTimeFormatter.ofPattern("MMMM", Locale.KOREAN);

  public static List<String> createMonthLabels(
      LocalDate baseMonth, final int monthCount, DateTimeFormatter formatter) {
    return IntStream.rangeClosed(0, monthCount)
        .mapToObj(baseMonth::plusMonths)
        .sorted(Comparator.comparingInt(LocalDate::getMonthValue))
        .map(date -> date.format(formatter))
        .toList();
  }

  public static List<String> createMonthLabels(LocalDate baseMonth, final int monthCount) {
    return createMonthLabels(baseMonth, monthCount, MONTH_FORMATTER);
  }
}
