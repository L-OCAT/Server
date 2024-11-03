package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.dto.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQRepository {

  Page<UserInfoDto> findAllByCriteria(AdminUserSearchCriteria criteria, Pageable pageable);
}
