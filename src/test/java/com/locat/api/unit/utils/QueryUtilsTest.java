package com.locat.api.unit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.global.utils.QueryUtils;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class QueryUtilsTest {

  private static final int TEST_PAGE_NUMBER = 0;
  private static final int TEST_PAGE_SIZE = 10;
  private static final long TEST_TOTAL_COUNT = 100L;

  private Pageable pageable;
  private List<String> content;

  @BeforeEach
  void setUp() {
    this.pageable = PageRequest.of(TEST_PAGE_NUMBER, TEST_PAGE_SIZE);
    this.content = List.of("item1", "item2", "item3");
  }

  @Test
  @DisplayName("리스트를 Page 객체로 변환할 수 있다")
  void shouldConvertListToPage() {
    // Given
    long totalCount = TEST_TOTAL_COUNT;

    // When
    Page<String> result = QueryUtils.toPage(this.content, this.pageable, totalCount);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo(this.content);
    assertThat(result.getPageable()).isEqualTo(this.pageable);
    assertThat(result.getTotalElements()).isEqualTo(totalCount);
    assertThat(result.getSize()).isEqualTo(TEST_PAGE_SIZE);
    assertThat(result.getNumber()).isEqualTo(TEST_PAGE_NUMBER);
  }

  @Test
  @DisplayName("빈 리스트를 Page 객체로 변환할 수 있다")
  void shouldConvertEmptyListToPage() {
    // Given
    List<String> emptyContent = Collections.emptyList();
    long totalCount = 0L;

    // When
    Page<String> result = QueryUtils.toPage(emptyContent, this.pageable, totalCount);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEmpty();
    assertThat(result.getPageable()).isEqualTo(this.pageable);
    assertThat(result.getTotalElements()).isZero();
    assertThat(result.getSize()).isEqualTo(TEST_PAGE_SIZE);
    assertThat(result.getNumber()).isEqualTo(TEST_PAGE_NUMBER);
  }

  @Test
  @DisplayName("페이지 크기보다 작은 리스트를 Page 객체로 변환할 수 있다")
  void shouldConvertPartialListToPage() {
    // Given
    List<String> partialContent = List.of("item1");
    long totalCount = 1L;

    // When
    Page<String> result = QueryUtils.toPage(partialContent, this.pageable, totalCount);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getPageable()).isEqualTo(this.pageable);
    assertThat(result.getTotalElements()).isEqualTo(totalCount);
    assertThat(result.getSize()).isEqualTo(TEST_PAGE_SIZE);
    assertThat(result.getNumber()).isEqualTo(TEST_PAGE_NUMBER);
  }
}
