package com.locat.api.domain.admin.service.impl;

import com.locat.api.domain.admin.dto.*;
import com.locat.api.domain.admin.service.AdminStatisticService;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.infra.persistence.admin.AdminDashboardQRepository;
import com.locat.api.infra.persistence.admin.UserStatisticQRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final AdminDashboardQRepository adminDashboardQRepository;
  private final UserStatisticQRepository userStatisticQRepository;
  private final UserService userService;
  private final FoundItemService foundItemService;
  private final LostItemService lostItemService;

  @Override
  public AdminDashboardSummaryDto getSummary() {
    return this.adminDashboardQRepository.getSummary();
  }

  @Override
  public AdminMonthlyItemStatDto getMonthlyItemStat() {
    return this.adminDashboardQRepository.getMonthlyItemStat();
  }

  @Override
  public List<AdminItemStatByCategoryDto> getStatByCategory() {
    return this.adminDashboardQRepository.getStatByCategory();
  }

  @Override
  public AdminUserStatDto getEndUserStat(Long userId) {
    User user = this.findUser(userId);
    return this.userStatisticQRepository.getUserStat(user);
  }

  @Override
  public List<AdminUserFoundItemStatDto> getUserFoundItemStat(Long userId) {
    User user = this.findUser(userId);
    return this.foundItemService.findTop10ByEndUser(user).stream()
        .map(AdminUserFoundItemStatDto::fromEntity)
        .toList();
  }

  @Override
  public List<AdminUserLostItemStatDto> getUserLostItemStat(Long userId) {
    User user = this.findUser(userId);
    return this.lostItemService.findTop10ByEndUser(user).stream()
        .map(AdminUserLostItemStatDto::fromEntity)
        .toList();
  }

  private User findUser(Long userId) {
    return this.userService
        .findById(userId)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }
}
