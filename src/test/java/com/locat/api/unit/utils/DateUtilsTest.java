package com.locat.api.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.global.utils.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  private static final DateTimeFormatter TEST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

  @Test
  @DisplayName("기본 포맷으로 월 레이블을 생성할 수 있다")
  void shouldCreateMonthLabelsWithDefaultFormatter() {
    // Given
    LocalDate baseMonth = LocalDate.of(2024, 3, 1);
    int monthCount = 2;

    // When
    List<String> labels = DateUtils.createMonthLabels(baseMonth, monthCount);

    // Then
    assertThat(labels).hasSize(3).containsExactly("3월", "4월", "5월");
  }

  @Test
  @DisplayName("사용자 정의 포맷으로 월 레이블을 생성할 수 있다")
  void shouldCreateMonthLabelsWithCustomFormatter() {
    // Given
    LocalDate baseMonth = LocalDate.of(2024, 3, 1);
    int monthCount = 2;

    // When
    List<String> labels = DateUtils.createMonthLabels(baseMonth, monthCount, TEST_FORMATTER);

    // Then
    assertThat(labels).hasSize(3).containsExactly("2024-03", "2024-04", "2024-05");
  }

  @Test
  @DisplayName("월이 순환되는 경우에도 올바른 순서로 레이블을 생성해야 한다")
  void shouldCreateMonthLabelsInCorrectOrderWhenMonthsWrap() {
    // Given
    LocalDate baseMonth = LocalDate.of(2024, 11, 1);
    int monthCount = 2;

    // When
    List<String> labels = DateUtils.createMonthLabels(baseMonth, monthCount, TEST_FORMATTER);

    // Then
    assertThat(labels).hasSize(3).containsExactly("11월", "12월", "01월");
  }
}
