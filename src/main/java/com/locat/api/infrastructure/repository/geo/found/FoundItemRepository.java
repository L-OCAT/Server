package com.locat.api.infrastructure.repository.geo.found;

import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.user.entity.EndUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {

  List<FoundItem> findTop10ByUserOrderByCreatedAtDesc(EndUser user);
}
