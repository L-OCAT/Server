package com.locat.api.infra.persistence.user;

import com.locat.api.domain.user.entity.association.UserEndpoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEndpointRepository extends JpaRepository<UserEndpoint, Long> {
  List<UserEndpoint> findByUserId(Long userId);
}
