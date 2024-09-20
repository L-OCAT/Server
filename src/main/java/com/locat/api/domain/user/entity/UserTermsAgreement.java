package com.locat.api.domain.user.entity;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import com.locat.api.domain.terms.entity.Terms;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "user_terms_agreement",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_terms",
          columnNames = {"user_id", "terms_id"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsAgreement extends SecuredBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "terms_id", nullable = false)
  private Terms terms;

  public static UserTermsAgreement of(User user, Terms agreement) {
    return UserTermsAgreement.builder().user(user).terms(agreement).build();
  }
}
