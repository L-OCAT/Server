DROP TABLE end_user;
DROP TABLE admin_user;
DROP TABLE user;

CREATE TABLE user
(
    id                  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    oauth_id            VARCHAR(100)            NOT NULL COMMENT '소셜 로그인 ID',
    oauth_type          ENUM ('KAKAO', 'APPLE') NOT NULL COMMENT 'OAuth2 서비스 제공자 구분',
    email               VARCHAR(100)            NOT NULL COMMENT '이메일',
    email_hash          VARCHAR(255)            NOT NULL COMMENT '이메일 해시값',
    password            VARCHAR(60)                                      DEFAULT null COMMENT '관리자 비밀번호',
    is_password_expired TINYINT(1)              NOT NULL                 DEFAULT 1 COMMENT '관리자 비밀번호 만료 여부',
    nickname            VARCHAR(100)            NOT NULL COMMENT '닉네임',
    profile_image       VARCHAR(255)                                     DEFAULT null COMMENT '프로필 이미지',
    user_type           ENUM ('USER', 'MANAGER', 'ADMIN', 'SUPER_ADMIN') DEFAULT 'USER' COMMENT '권한',
    status_type         ENUM ('ACTIVE', 'INACTIVE', 'BANNED')            DEFAULT 'ACTIVE' COMMENT '상태',
    created_at          DATETIME                                         DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by          INT UNSIGNED            NULL COMMENT '생성자',
    updated_at          DATETIME                                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by          INT UNSIGNED                                     DEFAULT NULL COMMENT '수정자',
    deleted_at          DATETIME                                         DEFAULT null COMMENT '탈퇴일',
    UNIQUE KEY unique_oauth_id (oauth_id),
    UNIQUE KEY unique_email (email),
    UNIQUE KEY unique_nickname (nickname)
) COMMENT '사용자 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

TRUNCATE TABLE user_terms_agreement;
TRUNCATE TABLE admin_device_id;
TRUNCATE TABLE user_setting;
TRUNCATE TABLE user_withdrawal_log;

