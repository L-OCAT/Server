package com.locat.api.domain.geo.found.service;

import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.user.entity.EndUser;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.web.multipart.MultipartFile;

/** 습득물 도메인 관련 서비스 */
public interface FoundItemService {

  /**
   * ID로 습득물을 조회합니다.
   *
   * @param id 습득물 ID
   * @return 습득물
   */
  FoundItem findById(final Long id);

  /**
   * 조건에 맞는 습득물 목록을 조회합니다.
   *
   * @param userId 사용자 ID
   * @param searchDto 습득물 검색 조건
   * @param pageable 페이지 요청 정보
   * @return 위치 정보를 포함하는 습득물 목록
   */
  GeoPage<FoundItem> findAllByCondition(
      final Long userId, FoundItemSearchDto searchDto, Pageable pageable);

  /**
   * 사용자가 등록한 습득물 목록을 조회합니다.
   *
   * @param user 목록을 조회할 사용자
   * @return 사용자가 등록한 습득물 목록 (최대 10개)
   */
  List<FoundItem> findTop10ByEndUser(EndUser user);

  /**
   * 습득물 정보를 등록합니다.
   *
   * @param userId 등록을 요청하는 사용자의 ID
   * @param request 등록할 습득물 정보
   * @param foundItemImage 습득물의 이미지
   * @return 등록된 습득물의 ID
   */
  Long register(
      final Long userId, final FoundItemRegisterDto request, final MultipartFile foundItemImage);
}
