package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.lost.dto.internal.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.entity.LostItemStatusType;
import com.locat.api.domain.user.entity.User;
import com.locat.api.helper.TestDataFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class LostItemEntityTest {

  private static final String ITEM_NAME = "iPhone 16 Pro Max";
  private static final String DESCRIPTION = "투명 케이스, 데저트 티타늄 색상입니다.";
  private static final int GRATUITY = 5;
  private static final String IMAGE_URL = "https://example.com/image.jpg";
  private static final LostItemStatusType STATUS_TYPE = LostItemStatusType.REGISTERED;
  @InjectMocks private LostItem lostItem;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Category mockCategory = TestDataFactory.createCategory("전자기기");
    Set<ColorCode> mockColorCodes =
        Set.of(ColorCode.of("#FF0000", "Red"), ColorCode.of("#000000", "Black"));
    LostItemRegisterDto mockRegisterDto =
        TestDataFactory.create(ITEM_NAME, DESCRIPTION, GRATUITY, true);

    // Given
    this.lostItem =
        LostItem.of(mock(User.class), mockCategory, mockColorCodes, mockRegisterDto, IMAGE_URL);
  }

  @Test
  @DisplayName("of() 메서드로 생성한 LostItem 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testOf() {
    // When & Then
    assertThat(this.lostItem).isNotNull();
    assertThat(this.lostItem.getId()).isNull(); // Auto Increment되는 값이므로 null
    assertThat(this.lostItem.getCategory()).isNotNull();
    assertThat(this.lostItem.getCategoryId()).isEqualTo(1L);
    assertThat(this.lostItem.getIsWillingToPayGratuity()).isTrue();
    assertThat(this.lostItem.getGratuity()).isEqualTo(GRATUITY);
    assertThat(this.lostItem.getUser()).isNotNull();
    assertThat(this.lostItem.getName()).isEqualTo(ITEM_NAME);
    assertThat(this.lostItem.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(this.lostItem.getLocation()).isNotNull();
    assertThat(this.lostItem.getLostAt()).isNotNull();
    assertThat(this.lostItem.getStatusType()).isEqualTo(STATUS_TYPE);
    assertThat(this.lostItem.getColorCodes()).hasSize(2);
    assertThat(this.lostItem.getImageUrl()).isEqualTo(IMAGE_URL);
  }

  @Test
  @DisplayName("LostItem의 색상 이름 리스트가 정확히 반환되는지 확인한다.")
  void testGetColorNames() {
    // When
    Set<String> colorNames = this.lostItem.getColorNames();

    // Then
    assertThat(colorNames).containsExactlyInAnyOrder("Red", "Black");
  }

  @Test
  @DisplayName("카테고리, 색상 코드 값에 따라 매칭 가능 여부를 적절하게 반환해야 한다.")
  void testIsMatchable() {
    // Given
    Category category = TestDataFactory.createCategory(2L, "기타");
    Set<ColorCode> colorCodes = Set.of(ColorCode.of("#000000", "기타"));
    LostItemRegisterDto registerDto =
        TestDataFactory.create(ITEM_NAME, DESCRIPTION, GRATUITY, true);
    LostItem nonMatchableItem =
        LostItem.of(mock(User.class), category, colorCodes, registerDto, IMAGE_URL);

    // When & Then
    assertThat(this.lostItem.isMatchable()).isTrue();
    assertThat(nonMatchableItem.isMatchable()).isFalse();
  }
}
