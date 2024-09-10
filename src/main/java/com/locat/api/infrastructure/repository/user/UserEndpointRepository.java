package com.locat.api.infrastructure.repository.user;

import com.locat.api.domain.user.entity.UserEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEndpointRepository extends JpaRepository<UserEndpoint, Long> {
    List<UserEndpoint> findByUserId(Long userId);
}
