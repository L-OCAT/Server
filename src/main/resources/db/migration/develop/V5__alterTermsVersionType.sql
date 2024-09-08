ALTER TABLE terms
    MODIFY version DECIMAL(3, 1) NOT NULL COMMENT '약관 버전';

CREATE TABLE faq
(
    id         INT UNSIGNED AUTO_INCREMENT COMMENT 'FAQ ID',
    type       ENUM ('GENERAL', 'USER', 'LOST_FOUND', 'SERVICE', 'MISCELLANEOUS') NOT NULL COMMENT 'FAQ 타입',
    title      VARCHAR(100)                                                       NOT NULL COMMENT 'FAQ 제목',
    content    TEXT                                                               NOT NULL COMMENT 'FAQ 내용',
    created_by BIGINT                                                             NOT NULL COMMENT '생성자',
    created_at datetime                                                           NOT NULL COMMENT '생성일시',
    updated_by BIGINT                                                             NOT NULL COMMENT '수정자',
    updated_at datetime                                                           NOT NULL COMMENT '수정일시',
    CONSTRAINT pk_faq PRIMARY KEY (id)
) COMMENT 'FAQ'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

-- FAQ 초기 데이터 추가
INSERT INTO faq (type, title, content, created_by, created_at, updated_by, updated_at)
VALUES ('GENERAL', 'FAQ 제목', 'FAQ 내용', 1, NOW(), 1, NOW()),
       ('USER', 'FAQ 제목', 'FAQ 내용', 1, NOW(), 1, NOW()),
       ('LOST_FOUND', 'FAQ 제목', 'FAQ 내용', 1, NOW(), 1, NOW()),
       ('SERVICE', 'FAQ 제목', 'FAQ 내용', 1, NOW(), 1, NOW()),
       ('MISCELLANEOUS', 'FAQ 제목', 'FAQ 내용', 1, NOW(), 1, NOW());

-- 약관 초기 데이터 추가
INSERT INTO terms (type, title, content, version, created_at, created_by, updated_at, updated_by)
VALUES ('TERMS_OF_SERVICE', '서비스 이용약관', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1),
       ('PRIVACY_POLICY', '개인정보 처리방침', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1),
       ('OVER_14_POLICY', '만 14세 이상', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1),
       ('LOCATION_POLICY', '위치기반 서비스 이용약관', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1),
       ('MARKETING_POLICY', '마케팅 정보 수신 동의', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1),
       ('MARKETING_KAKAO_POLICY', '카카오톡 마케팅 정보 수신 동의', 'PLACEHOLDER', 1, NOW(), 1, NOW(), 1);
