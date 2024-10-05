package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.helper.TestDataFactory;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.entity.FoundItemStatusType;
import com.locat.api.domain.user.entity.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class FoundItemEntityTest {

  private static final String CUSTODY_LOCATION = "종로구청";
  private static final String ITEM_NAME = "iPhone 16 Pro Max";
  private static final String DESCRIPTION = "투명 케이스, 데저트 티타늄 색상입니다.";
  private static final String IMAGE_URL = "http://example.com/image.jpg";
  private static final FoundItemStatusType STATUS_TYPE = FoundItemStatusType.REGISTERED;
  @InjectMocks private FoundItem foundItem;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Mock 데이터 준비
    User mockUser = Mockito.mock(User.class);
    Category mockCategory = Mockito.mock(Category.class);
    Set<ColorCode> mockColorCodes =
        new HashSet<>(Set.of(ColorCode.of("#FF0000", "Red"), ColorCode.of("#000000", "Black")));
    FoundItemRegisterDto mockRegisterDto =
        TestDataFactory.create(ITEM_NAME, DESCRIPTION, CUSTODY_LOCATION);

    // Given
    this.foundItem =
        FoundItem.of(mockUser, mockCategory, mockColorCodes, mockRegisterDto, IMAGE_URL);
  }

  @Test
  @DisplayName("of() 메서드로 생성한 FoundItem 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testOf() {
    // When & Then
    assertThat(this.foundItem).isNotNull();
    assertThat(this.foundItem.getCustodyLocation()).isEqualTo(CUSTODY_LOCATION);
    assertThat(this.foundItem.getName()).isEqualTo(ITEM_NAME);
    assertThat(this.foundItem.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(this.foundItem.getFoundAt()).isNotNull();
    assertThat(this.foundItem.getStatusType()).isEqualTo(STATUS_TYPE);
    assertThat(this.foundItem.getColorCodes()).hasSize(2);
    assertThat(this.foundItem.getImageUrl()).isEqualTo(IMAGE_URL);
  }

  @Test
  @DisplayName("FoundItem의 색상 이름 리스트가 정확히 반환되는지 확인한다.")
  void testGetColorNames() {
    // When
    Set<String> colorNames = this.foundItem.getColorNames();

    // Then
    assertThat(colorNames).containsExactlyInAnyOrder("Red", "Black");
  }
}
