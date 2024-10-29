package com.locat.api.infrastructure.repository.geo.lost;

import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {

  List<LostItem> findTop10ByUserOrderByCreatedAtDesc(User user);
}
