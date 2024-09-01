CREATE TABLE lost_item
(
    id                         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '분실물 ID',
    user_id                    BIGINT UNSIGNED NOT NULL COMMENT '사용자 ID',
    category_id                BIGINT UNSIGNED NOT NULL COMMENT '카테고리 ID',
    `name`                     VARCHAR(255)    NOT NULL COMMENT '물품명',
    `description`              VARCHAR(500) COMMENT '설명',
    is_willing_to_pay_gratuity BOOLEAN      DEFAULT FALSE COMMENT '보상금 지급 여부',
    gratuity                   INT UNSIGNED DEFAULT 0 COMMENT '보상 비율',
    location                   POINT SRID 4326 NOT NULL COMMENT '분실 위치',
    lost_at                    DATETIME COMMENT '분실 일시',
    status_type                ENUM ('REGISTERED', 'CLAIMED', 'RETURNED', 'NOT_RETURNED', 'DELETED') DEFAULT 'REGISTERED' COMMENT '분실물 상태',
    image_url                  VARCHAR(255) COMMENT '이미지 URL',
    created_at                 DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by                 INT UNSIGNED    NOT NULL COMMENT '생성자',
    updated_at                 DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by                 INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    deleted_at                 DATETIME     DEFAULT null COMMENT '탈퇴일',
    PRIMARY KEY (id),
    SPATIAL INDEX idx_lost_item_location (location),
    CHECK ( gratuity >= 0 AND gratuity <= 5 )
) COMMENT '분실물 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE found_item
(
    id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '습득물 ID',
    user_id          BIGINT UNSIGNED NOT NULL COMMENT '사용자 ID',
    category_id      BIGINT UNSIGNED NOT NULL COMMENT '카테고리 ID',
    `name`           VARCHAR(50)     NOT NULL COMMENT '물품명',
    `description`    VARCHAR(500) COMMENT '설명',
    custody_location VARCHAR(100) COMMENT '보관 장소',
    location         POINT SRID 4326 NOT NULL COMMENT '습득 위치',
    found_at         DATETIME COMMENT '습득 일시',
    status_type      ENUM ('REGISTERED', 'MATCHED', 'RETURNED', 'NOT_RETURNED', 'DELETED') DEFAULT 'REGISTERED' COMMENT '분실물 상태',
    image_url        VARCHAR(255) COMMENT '이미지 URL',
    created_at       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by       INT UNSIGNED    NOT NULL COMMENT '생성자',
    updated_at       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by       INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    deleted_at       DATETIME     DEFAULT null COMMENT '탈퇴일',
    PRIMARY KEY (id),
    SPATIAL INDEX idx_found_item_location (location)
) COMMENT '습득물 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE color_code
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '색상 코드 ID',
    `name`     VARCHAR(50)     NOT NULL COMMENT '색상명',
    hex_code   VARCHAR(7)      NOT NULL COMMENT 'HEX 코드',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED    NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    PRIMARY KEY (id)
) COMMENT '색상 코드',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE item_color_code
(
    item_type  ENUM ('LOST', 'FOUND') NOT NULL COMMENT '물품 구분',
    item_id    BIGINT UNSIGNED        NOT NULL COMMENT '물품 ID',
    color_id   BIGINT UNSIGNED        NOT NULL COMMENT '색상 ID',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED           NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    PRIMARY KEY (item_type, item_id, color_id)
) COMMENT '물품 색상 코드',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;
