CREATE TABLE admin_activity
(
    id               INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT 'Primary Key',
    email            VARCHAR(100)                NOT NULL COMMENT '관리자 이메일',
    super_admin_only BOOLEAN                     NOT NULL COMMENT '최고 관리자 전용 여부',
    http_method      VARCHAR(10)                 NOT NULL COMMENT 'HTTP Method',
    uri              VARCHAR(255)                NOT NULL COMMENT '요청 URI',
    http_status      INT                         NOT NULL COMMENT '응답 HTTP 상태 코드',
    is_successful    BOOLEAN                     NOT NULL COMMENT '처리 성공 여부',
    remote_address   VARCHAR(15)                 NOT NULL COMMENT '요청자 IP',
    user_agent       VARCHAR(255)                NOT NULL COMMENT 'HTTP User-Agent',
    created_at       datetime                    NOT NULL COMMENT '생성일시',
    updated_at       datetime                    NOT NULL COMMENT '수정일시',
    CONSTRAINT pk_admin_activity PRIMARY KEY (id)
) COMMENT '관리자 활동 이력'
    CHARSET = 'UTF8MB4',
    COLLATE = 'UTF8MB4_GENERAL_CI',
    ENGINE = InnoDB;