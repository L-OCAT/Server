CREATE TABLE user_endpoint
(
    id               BIGINT UNSIGNED         NOT NULL AUTO_INCREMENT COMMENT '엔드포인트 ID',
    user_id          BIGINT UNSIGNED         NOT NULL COMMENT '사용자 ID',
    device_token     VARCHAR(255)            NOT NULL COMMENT '디바이스 토큰',
    platform         ENUM ('IOS', 'ANDROID') NOT NULL COMMENT '플랫폼 타입',
    endpoint_arn     VARCHAR(500)            NOT NULL COMMENT '플랫폼 엔드포인트 arn',
    subscription_arn VARCHAR(500)            NOT NULL COMMENT '구독 arn',
    created_at       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by       INT UNSIGNED            NOT NULL COMMENT '생성자',
    updated_at       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by       INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    PRIMARY KEY (id)
) COMMENT '사용자 엔드포인트 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

ALTER TABLE user ADD email_hash VARCHAR(255) NOT NULL COMMENT '이메일 해시값' AFTER email;