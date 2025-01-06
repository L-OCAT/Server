ALTER TABLE user
    ADD oauth_type ENUM ('KAKAO', 'APPLE') NOT NULL COMMENT 'OAuth2 서비스 제공자 구분' AFTER oauth_id;
