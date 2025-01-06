ALTER TABLE terms
    MODIFY COLUMN version DOUBLE NOT NULL COMMENT '약관 버전';
ALTER TABLE terms
    ADD COLUMN is_required BOOLEAN DEFAULT FALSE NOT NULL COMMENT '필수 여부';

CREATE TABLE terms_revision_history
(
    id            INT UNSIGNED AUTO_INCREMENT                                                                                                    NOT NULL COMMENT '약관 개정 이력 ID',
    type          ENUM ('OVER_14_POLICY', 'TERMS_OF_SERVICE', 'PRIVACY_POLICY', 'LOCATION_POLICY', 'MARKETING_POLICY', 'MARKETING_KAKAO_POLICY') NULL COMMENT '약관 타입',
    is_required   BOOLEAN DEFAULT FALSE                                                                                                          NOT NULL COMMENT '필수 여부',
    title         VARCHAR(150)                                                                                                                   NOT NULL COMMENT '약관 제목',
    content       TEXT                                                                                                                           NOT NULL COMMENT '약관 내용',
    version       DOUBLE  DEFAULT 1.0                                                                                                            NOT NULL COMMENT '약관 버전',
    revision_note TEXT                                                                                                                           NOT NULL COMMENT '약관 개정 이유',
    created_by    BIGINT                                                                                                                         NOT NULL COMMENT '생성자',
    created_at    datetime                                                                                                                       NOT NULL COMMENT '생성일시',
    updated_by    BIGINT                                                                                                                         NOT NULL COMMENT '수정자',
    updated_at    datetime                                                                                                                       NOT NULL COMMENT '수정일시',
    CONSTRAINT pk_terms_revision_history PRIMARY KEY (id)
) COMMENT '약관 개정 이력(SNAPSHOT)'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;