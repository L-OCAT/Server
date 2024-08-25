package com.locat.api.domain.geo.lost.service.impl;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.lost.dto.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.LostItemSearchDto;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.file.FileService;
import com.locat.api.infrastructure.repository.geo.GeoItemQRepository;
import com.locat.api.infrastructure.repository.geo.lost.LostItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class LostItemServiceImpl implements LostItemService {

  private static final String LOST_ITEM_IMAGE_DIRECTORY = "items/losts";

  private final LostItemRepository lostItemRepository;
  private final GeoItemQRepository<LostItem> lostItemQRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final FileService fileService;

  @Override
  public LostItem findById(Long id) {
    return this.lostItemRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_ITEM_LOST));
  }

  @Override
  public GeoPage<LostItem> findAllByCondition(
      Long userId, LostItemSearchDto searchDto, Pageable pageable) {
    return this.lostItemQRepository.findByCondition(userId, searchDto, pageable);
  }

  @Override
  public Long register(Long userId, LostItemRegisterDto registerDto, MultipartFile foundItemImage) {
    final User user = this.userService.findById(userId);
    final Category category =
        this.categoryService.findById(registerDto.categoryId()).orElse(Category.ofCustom());
    final String imageUrl = this.fileService.upload(LOST_ITEM_IMAGE_DIRECTORY, foundItemImage);
    return this.lostItemRepository.save(LostItem.of(user, category, registerDto, imageUrl)).getId();
  }
}
