CREATE TABLE geo_item_address
(
    id                 INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT 'PK',
    item_id            INT UNSIGNED                NOT NULL COMMENT '아이템 ID',
    item_type          ENUM ('LOST', 'FOUND')      NOT NULL COMMENT '아이템 타입',
    latitude           DECIMAL(11, 8)              NOT NULL COMMENT '위도',
    longitude          DECIMAL(11, 8)              NOT NULL COMMENT '경도',
    region1            VARCHAR(100)                NOT NULL COMMENT '지역1Depth(시/도 단위)',
    region2            VARCHAR(100)                NOT NULL COMMENT '지역2Depth(시/군/구 단위)',
    region3            VARCHAR(100)                NOT NULL COMMENT '지역3Depth(읍/면/동 단위)',
    lot_number_address VARCHAR(255)                NOT NULL COMMENT '지번 주소',
    road_address       VARCHAR(255)                NULL COMMENT '도로명 주소',
    building_name      VARCHAR(100)                NULL COMMENT '건물명',
    created_at         datetime                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at         datetime                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    CONSTRAINT pk_geo_item_address PRIMARY KEY (id),
    CONSTRAINT uk_geo_item_address UNIQUE (item_id, item_type)
) COMMENT '분실물 주소'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;