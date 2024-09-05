ALTER TABLE terms
    MODIFY version DECIMAL(3, 1) NOT NULL COMMENT '약관 버전';

CREATE TABLE faq
(
    type       ENUM ('GENERAL', 'USER', 'LOST_FOUND', 'SERVICE', 'MISCELLANEOUS') NOT NULL COMMENT 'FAQ 타입',
    title      VARCHAR(100)                                                       NOT NULL COMMENT 'FAQ 제목',
    content    TEXT                                                               NOT NULL COMMENT 'FAQ 내용',
    created_by BIGINT                                                             NOT NULL COMMENT '생성자',
    created_at datetime                                                           NOT NULL COMMENT '생성일시',
    updated_by BIGINT                                                             NOT NULL COMMENT '수정자',
    updated_at datetime                                                           NOT NULL COMMENT '수정일시',
    CONSTRAINT pk_faq PRIMARY KEY (type)
) COMMENT 'FAQ'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB
