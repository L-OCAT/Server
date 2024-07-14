CREATE TABLE user
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    oauth_id      VARCHAR(100) NOT NULL COMMENT '소셜 로그인 ID',
    email         VARCHAR(100) NOT NULL COMMENT '이메일',
    nickname      VARCHAR(100) NOT NULL COMMENT '닉네임',
    profile_image VARCHAR(255)                          DEFAULT null COMMENT '프로필 이미지',
    user_type     ENUM ('USER', 'ADMIN')                DEFAULT 'USER' COMMENT '권한',
    status_type   ENUM ('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE' COMMENT '상태',
    created_at    DATETIME                              DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by    INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at    DATETIME                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by    INT UNSIGNED                          DEFAULT NULL COMMENT '수정자',
    deleted_at    DATETIME                              DEFAULT null COMMENT '탈퇴일',
    UNIQUE KEY unique_oauth_id (oauth_id),
    UNIQUE KEY unique_email (email),
    UNIQUE KEY unique_nickname (nickname)
) COMMENT '사용자 정보',
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE user_setting
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '설정 ID',
    user_id    INT UNSIGNED NOT NULL COMMENT '사용자 ID',
    setting_id INT UNSIGNED NOT NULL COMMENT '설정 ID',
    value      TEXT         NOT NULL COMMENT '설정 값',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    UNIQUE KEY unique_user_setting (user_id, setting_id)
) COMMENT '사용자 설정'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;

CREATE TABLE user_withdrawal_log
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '탈퇴 ID',
    user_id    INT UNSIGNED NOT NULL COMMENT '사용자 ID',
    reason     TINYTEXT     NOT NULL COMMENT '탈퇴 사유',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자'
) COMMENT '사용자 탈퇴 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE user_terms_agreement
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '동의 ID',
    user_id    INT UNSIGNED NOT NULL COMMENT '사용자 ID',
    terms_id   INT UNSIGNED NOT NULL COMMENT '약관 ID',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    UNIQUE KEY unique_user_terms (user_id, terms_id)
) COMMENT '사용자 약관 동의 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;

CREATE TABLE terms
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '약관 ID',
    type       ENUM ('SERVICE', 'PRIVACY', 'LOCATION', 'MARKETING') NOT NULL COMMENT '약관 종류',
    title      VARCHAR(150)                                         NOT NULL COMMENT '약관 제목',
    content    TEXT                                                 NOT NULL COMMENT '약관 내용',
    version    VARCHAR(30)                                          NOT NULL COMMENT '약관 버전',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED                                         NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    UNIQUE KEY unique_type_version (type, version)
) COMMENT '약관 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE category
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 ID',
    name       VARCHAR(80)  NOT NULL COMMENT '카테고리 이름',
    parent_id  INT UNSIGNED DEFAULT null COMMENT '상위 카테고리 ID',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자'
) COMMENT '카테고리 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

/*
CREATE TABLE lost
(
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '분실물 ID',
    user_id      INT UNSIGNED NOT NULL COMMENT '사용자 ID',
    category_id  INT UNSIGNED NOT NULL COMMENT '카테고리 ID',
    color_id     INT UNSIGNED NOT NULL COMMENT '색상 ID',
    title        VARCHAR(100) NOT NULL COMMENT '제목',
    content      TEXT         NOT NULL COMMENT '내용',
    location     VARCHAR(100) NOT NULL COMMENT '분실 장소',
    lost_at      DATETIME     NOT NULL COMMENT '분실 일자',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    created_by   INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_by   INT UNSIGNED DEFAULT NULL COMMENT '수정자'
#     FOREIGN KEY (user_id) REFERENCES user (id),
#     FOREIGN KEY (category_id) REFERENCES category (id),
#     FOREIGN KEY (color_id) REFERENCES color (id)
) COMMENT '분실물 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;

CREATE TABLE found
(
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '습득물 ID',
    user_id      INT UNSIGNED NOT NULL COMMENT '사용자 ID',
    category_id  INT UNSIGNED NOT NULL COMMENT '카테고리 ID',
    title        VARCHAR(100) NOT NULL COMMENT '제목',
    content      TEXT         NOT NULL COMMENT '내용',
    location     VARCHAR(100) NOT NULL COMMENT '습득 장소',
    found_at     DATETIME     NOT NULL COMMENT '습득 일자',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    created_by   INT UNSIGNED NOT NULL COMMENT '생성자 ID',
    updated_by   INT UNSIGNED DEFAULT NULL COMMENT '수정자 ID'
#     FOREIGN KEY (user_id) REFERENCES user (id),
#     FOREIGN KEY (category_id) REFERENCES category (id)
) COMMENT '습득물 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;
*/

CREATE TABLE contact_center
(
    id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '분실물센터 ID',
    name       VARCHAR(100) NOT NULL COMMENT '분실물센터 이름',
    contact    VARCHAR(100) NOT NULL COMMENT '연락처',
    address    VARCHAR(255) NOT NULL COMMENT '주소',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by INT UNSIGNED DEFAULT NULL COMMENT '수정자'
) COMMENT '분실물센터 정보'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;

CREATE TABLE app_setting -- 앱 & 알림 설정
(
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '설정 ID',
    name          VARCHAR(100) NOT NULL COMMENT '설정 이름',
    default_value TEXT         NOT NULL COMMENT '설정 값',
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    created_by    INT UNSIGNED NOT NULL COMMENT '생성자',
    updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    updated_by    INT UNSIGNED DEFAULT NULL COMMENT '수정자',
    UNIQUE KEY unique_name (name)
) COMMENT '애플리케이션 설정'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ROW_FORMAT = Dynamic,
    ENGINE = InnoDB;
