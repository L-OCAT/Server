package com.locat.api.domain.geo.found.service;

import com.locat.api.domain.geo.found.dto.FoundItemRegisterRequest;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface FoundItemService {

  FoundItem findById(final Long id);

  Page<FoundItem> findAllByCondition(FoundItemSearchDto searchDto);

  /**
   * 습득물 정보를 등록합니다.
   *
   * @param userId 등록을 요청하는 사용자의 ID
   * @param request 등록할 습득물 정보
   * @param foundItemImage 습득물의 이미지
   * @return 등록된 습득물의 ID
   */
  Long register(
      final Long userId,
      final FoundItemRegisterRequest request,
      final MultipartFile foundItemImage);
}
