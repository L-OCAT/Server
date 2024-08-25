-- PENDING 상태 추가 및 인증 완료 전까지 PENDING 상태로 설정
ALTER TABLE user
    MODIFY status_type ENUM ('PENDING', 'ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'PENDING' NULL COMMENT '상태';

-- 기획 내용에 맞게 약관 타입 수정
ALTER TABLE terms
    MODIFY type ENUM ('OVER_14_POLICY', 'TERMS_OF_SERVICE', 'PRIVACY_POLICY', 'LOCATION_POLICY', 'MARKETING_POLICY', 'MARKETING_KAKAO_POLICY') NOT NULL COMMENT '약관 종류';

-- 약관 템플릿 추가
INSERT INTO terms (type, title, content, version, created_at, created_by, updated_at, updated_by)
VALUES ('OVER_14_POLICY', '만 14세 이상 확인', '만 14세 이상 확인 내용', 1, NOW(), 1, NOW(), 1),
       ('TERMS_OF_SERVICE', '서비스 이용약관', '서비스 이용약관 내용', 1, NOW(), 1, NOW(), 1),
       ('PRIVACY_POLICY', '개인정보 처리방침', '개인정보 처리방침 내용', 1, NOW(), 1, NOW(), 1),
       ('LOCATION_POLICY', '위치기반 서비스 이용약관', '위치기반 서비스 이용약관 내용', 1, NOW(), 1, NOW(), 1),
       ('MARKETING_POLICY', '마케팅 정보 수신 동의', '마케팅 정보 수신 동의 내용', 1, NOW(), 1, NOW(), 1),
       ('MARKETING_KAKAO_POLICY', '카카오마케팅 정보 수신 동의', '카카오마케팅 정보 수신 동의 내용', 1, NOW(), 1, NOW(), 1);

-- 카테고리 추가
INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('직접 입력', null, 1, 1),
       ('지갑', null, 1, 1),
       ('가방', null, 1, 1),
       ('전자기기', null, 1, 1),
       ('의류', null, 1, 1),
       ('문구류', null, 1, 1),
       ('카드', null, 1, 1),
       ('귀금속', null, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('카드지갑', 2, 1, 1),
       ('단지갑', 2, 1, 1),
       ('중지갑', 2, 1, 1),
       ('장지갑', 2, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('에코백', 3, 1, 1),
       ('백팩', 3, 1, 1),
       ('숄더백', 3, 1, 1),
       ('토트백', 3, 1, 1),
       ('메신저백', 3, 1, 1),
       ('파우치', 3, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('휴대폰', 4, 1, 1),
       ('태블릿', 4, 1, 1),
       ('스마트워치', 4, 1, 1),
       ('이어폰', 4, 1, 1),
       ('카메라', 4, 1, 1),
       ('기타', 4, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('모자', 5, 1, 1),
       ('신발', 5, 1, 1),
       ('아우터', 5, 1, 1),
       ('상/하의', 5, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('도서', 6, 1, 1),
       ('서류', 6, 1, 1),
       ('학용품', 6, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('교통카드', 7, 1, 1),
       ('체크/신용카드', 7, 1, 1),
       ('주민등록증/운전면허증', 7, 1, 1);

INSERT INTO category (name, parent_id, created_by, updated_by)
VALUES ('반지', 8, 1, 1),
       ('귀걸이', 8, 1, 1),
       ('목걸이', 8, 1, 1),
       ('시계', 8, 1, 1);
