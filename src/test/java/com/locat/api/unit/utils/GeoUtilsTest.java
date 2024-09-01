package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

class GeoUtilsTest {

  @Test
  @DisplayName("두 좌표가 반경 내에 있다면, true를 반환해야 한다.")
  void testIsInRadiusTrue() {
    // Given
    Point centralPoint = new Point(37.5665, 126.9780); // 서울시청
    Point targetPoint = new Point(37.5719, 126.9768); // 광화문
    Distance radius = new Distance(1.0, Metrics.KILOMETERS);

    // When
    boolean result = GeoUtils.isInRadius(centralPoint, targetPoint, radius);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("두 좌표가 반경을 벗어난다면, false를 반환해야 한다.")
  void testIsInRadiusFalse() {
    // Given
    Point centralPoint = new Point(37.5665, 126.9780); // 서울시청
    Point targetPoint = new Point(37.7749, -122.4194); // 샌프란시스코

    Distance radius = new Distance(5000.0, Metrics.KILOMETERS);

    // When
    boolean result = GeoUtils.isInRadius(centralPoint, targetPoint, radius);

    // Then
    assertThat(result).isFalse();
  }

  @ParameterizedTest
  @CsvSource({
    "37.5665, 126.9780, 37.5665, 126.9780, 0.0", // 같은 좌표
    "37.5665, 126.9780, 37.5651, 126.9896, 1.034", // 근거리
    "37.5665, 126.9780, 37.7749, -122.4194, 9028.92" // 장거리
  })
  @DisplayName("두 좌표 간의 거리를 정확히 계산해야 한다.")
  void testCaculate(double lat1, double lon1, double lat2, double lon2, double expectedDistance) {
    // Given
    Point p1 = new Point(lat1, lon1);
    Point p2 = new Point(lat2, lon2);

    // When
    double result = GeoUtils.calculateDistanceBetween(p1, p2);

    // Then
    assertThat(result).isCloseTo(expectedDistance, within(0.01));
  }

  @Test
  @DisplayName("미터 단위의 거리를 킬로미터 단위로 정확히 변환해야 한다.")
  void testToKilometer() {
    // Given
    double meter = 1234.0;

    // When
    double result = GeoUtils.toKilometer(meter);

    // Then
    assertThat(result).isEqualTo(1.234);
  }
}
