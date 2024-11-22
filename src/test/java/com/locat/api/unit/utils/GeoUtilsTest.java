package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.locationtech.jts.geom.Point;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

class GeoUtilsTest {

  @Test
  @DisplayName("두 좌표가 반경 내에 있다면, true를 반환해야 한다.")
  void testIsInRadiusTrue() {
    // Given
    Point centralPoint = GeoUtils.toPoint(37.5665, 126.9780); // 서울시청
    Point targetPoint = GeoUtils.toPoint(37.5719, 126.9768); // 광화문
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
    Point centralPoint = GeoUtils.toPoint(37.5665, 126.9780); // 서울시청
    Point targetPoint = GeoUtils.toPoint(37.7749, -122.4194); // 샌프란시스코

    Distance radius = new Distance(5000.0, Metrics.KILOMETERS);

    // When
    boolean result = GeoUtils.isInRadius(centralPoint, targetPoint, radius);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("주어진 좌표가 대한민국 영토 내에 위치하는지 적절히 판단해야 한다.")
  void testIsInKorea() {
    // Given
    Point inKorea = GeoUtils.toPoint(37.5665, 126.9780); // 서울시청
    Point outKorea = GeoUtils.toPoint(37.7749, -122.4194); // 샌프란시스코

    // When
    boolean result1 = GeoUtils.isInKorea(inKorea);
    boolean result2 = GeoUtils.isInKorea(outKorea);

    // Then
    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
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
    Point p1 = GeoUtils.toPoint(lat1, lon1);
    Point p2 = GeoUtils.toPoint(lat2, lon2);

    // When
    double result = GeoUtils.calculateDistanceBetween(p1, p2);

    // Then
    assertThat(result).isCloseTo(expectedDistance, within(0.01));
  }

  @Test
  @DisplayName("Meter <-> Kilometer 간 변환을 정확히 수행해야 한다.")
  void testToKilometer() {
    // Given
    final double meter = 1234.0;
    final double kilometer = 1.234;

    // When
    double result1 = GeoUtils.toKilometer(meter);
    double result2 = GeoUtils.toMeter(kilometer);

    // Then
    assertThat(result1).isEqualTo(1.234);
    assertThat(result2).isEqualTo(1234.0);
  }
}
