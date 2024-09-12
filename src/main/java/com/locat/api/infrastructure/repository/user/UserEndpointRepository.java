package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.UserEndpoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEndpointRepository extends JpaRepository<UserEndpoint, Long> {
  List<UserEndpoint> findByUserId(Long userId);
}
