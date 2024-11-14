package com.locat.api.infra.persistence.user;

import com.locat.api.domain.user.dto.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQRepository {

  /**
   * 사용자 정보를 조회합니다.
   *
   * @param criteria 검색 조건
   * @param pageable 페이징 정보
   * @return 사용자 정보
   */
  Page<UserInfoDto> findAllByCriteria(AdminUserSearchCriteria criteria, Pageable pageable);
}
