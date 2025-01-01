package com.locat.api.unit.admin;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.locat.api.domain.admin.service.impl.AdminStatisticServiceImpl;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.infra.persistence.admin.AdminDashboardQRepository;
import com.locat.api.infra.persistence.admin.UserStatisticQRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class AdminStatisticServiceTest {

  @InjectMocks private AdminStatisticServiceImpl service;
  @Mock private AdminDashboardQRepository adminDashboardQRepository;
  @Mock private UserStatisticQRepository userStatisticQRepository;
  @Mock private UserService userService;
  @Mock private FoundItemService foundItemService;
  @Mock private LostItemService lostItemService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("대시보드 통계 정보를 조회할 수 있다.")
  void testGetSummary() {
    // Given
    // no given

    // When & Then
    assertThatCode(() -> this.service.getSummary()).doesNotThrowAnyException();
    assertThatCode(() -> this.service.getMonthlyItemStat()).doesNotThrowAnyException();
    assertThatCode(() -> this.service.getStatByCategory()).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("존재하는 사용자의 통계 정보를 조회할 수 있다.")
  void testGetEndUserStat() {
    // Given
    given(this.userService.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));

    // When & Then
    assertAll(
        () -> assertThatCode(() -> this.service.getEndUserStat(1L)).doesNotThrowAnyException(),
        () ->
            assertThatCode(() -> this.service.getUserFoundItemStat(1L)).doesNotThrowAnyException(),
        () ->
            assertThatCode(() -> this.service.getUserLostItemStat(1L)).doesNotThrowAnyException());
  }

  @Test
  @DisplayName("존재하지 않는 사용자의 통계 정보 조회 시, NoSuchEntityException를 던진다.")
  void testGetEndUserStatWithNonExistentUser() {
    // Given
    given(this.userService.findById(1L)).willReturn(Optional.empty());

    // When & Then
    assertAll(
        () ->
            assertThatThrownBy(() -> this.service.getEndUserStat(1L))
                .isExactlyInstanceOf(NoSuchEntityException.class),
        () ->
            assertThatThrownBy(() -> this.service.getUserFoundItemStat(1L))
                .isExactlyInstanceOf(NoSuchEntityException.class),
        () ->
            assertThatThrownBy(() -> this.service.getUserLostItemStat(1L))
                .isExactlyInstanceOf(NoSuchEntityException.class));
  }
}
