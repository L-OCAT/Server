package com.locat.api.domain.user.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "user_endpoint")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEndpoint extends SecuredBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private PlatformType platformType;

    @Column(name = "endpoint_arn", nullable = false)
    private String endpointArn;

    @Column(name = "subscription_arn", nullable = false)
    private String subscriptionArn;
}
