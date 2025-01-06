alter table found_item add column created_by int unsigned not null comment '생성자' after created_at;
alter table found_item add column updated_by int unsigned default null comment '수정자' after updated_at;

alter table item_color_code drop column created_by;
alter table item_color_code drop column updated_by;

drop table if exists item_color_code;

CREATE TABLE found_item_color_code
(
    item_id    BIGINT UNSIGNED        NOT NULL COMMENT '물품 ID',
    color_id   BIGINT UNSIGNED        NOT NULL COMMENT '색상 ID',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    PRIMARY KEY (item_id, color_id)
) COMMENT '습득물 색상 코드',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE lost_item_color_code
(
    item_id    BIGINT UNSIGNED        NOT NULL COMMENT '물품 ID',
    color_id   BIGINT UNSIGNED        NOT NULL COMMENT '색상 ID',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    PRIMARY KEY (item_id, color_id)
) COMMENT '분실물 색상 코드',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE INDEX idx_lost_item_category ON lost_item (category_id);
CREATE INDEX idx_found_item_category ON found_item (category_id);