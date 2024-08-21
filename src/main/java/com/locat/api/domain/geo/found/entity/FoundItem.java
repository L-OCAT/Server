package com.locat.api.domain.geo.found.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.*;
import org.springframework.data.geo.Point;

@Entity
@Getter
@Builder
@Table(name = "found_item")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoundItem extends SecuredBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "int UNSIGNED")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  private Long colorId;

  @Column(name = "item_name", length = 50)
  private String itemName;

  @Column(name = "description", length = 500)
  private String description;

  private String custodyLocation;

  private Point location;

  private ZonedDateTime foundAt;

  private String imageUrl;
}
