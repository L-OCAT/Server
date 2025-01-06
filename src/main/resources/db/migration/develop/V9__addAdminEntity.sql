CREATE TABLE admin_user
(
    id                  INT UNSIGNED NOT NULL COMMENT '관리자 ID',
    password            VARCHAR(60)  NOT NULL COMMENT '비밀번호',
    is_password_expired TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '비밀번호 만료 여부',
    CONSTRAINT pk_admin_user PRIMARY KEY (id)
) COMMENT '관리자 사용자 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

ALTER TABLE admin_user
    ADD CONSTRAINT fk_admin_user_on_id FOREIGN KEY (id) REFERENCES user (id);

CREATE TABLE admin_device_id
(
    id            INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '관리자 디바이스 ID',
    admin_user_id INT UNSIGNED                NOT NULL COMMENT '관리자 ID',
    device_id     VARCHAR(255)                NOT NULL COMMENT '디바이스의 FingerPrint',
    is_trusted    TINYINT(1)                  NOT NULL DEFAULT 0 COMMENT '신뢰 디바이스 여부',
    created_at    datetime                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at    datetime                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    CONSTRAINT pk_admin_device_id PRIMARY KEY (id)
) COMMENT '관리자 디바이스 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

ALTER TABLE admin_device_id
    ADD CONSTRAINT unique_admin_device UNIQUE (admin_user_id, device_id);

CREATE TABLE end_user
(
    id            INT UNSIGNED            NOT NULL COMMENT '사용자 ID',
    oauth_id      VARCHAR(100)            NOT NULL COMMENT '소셜 로그인 ID',
    oauth_type    enum ('KAKAO', 'APPLE') NULL COMMENT '소셜 로그인 타입',
    profile_image VARCHAR(255)            NULL COMMENT '프로필 이미지',
    CONSTRAINT pk_end_user PRIMARY KEY (id)
) COMMENT '일반 사용자 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

ALTER TABLE end_user
    ADD CONSTRAINT unique_oauth UNIQUE (oauth_type, oauth_id);

ALTER TABLE end_user
    ADD CONSTRAINT fk_end_user_on_id FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE user
    DROP KEY unique_oauth_id;

ALTER TABLE user
    DROP COLUMN oauth_id;
ALTER TABLE user
    DROP COLUMN oauth_type;
ALTER TABLE user
    DROP COLUMN profile_image;

alter table user
    modify user_type enum ('USER', 'ADMIN', 'MANAGER') default 'USER' null comment '권한';

