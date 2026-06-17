CREATE SCHEMA IF NOT EXISTS "user";
SET search_path TO "user";

CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    no       VARCHAR(20)                         NOT NULL,
    login_id VARCHAR(20)                         NOT NULL,
    password VARCHAR(255)                        NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE users IS '유저';
COMMENT ON COLUMN users.id IS '사용자 고유 ID';
COMMENT ON COLUMN users.no IS '사용자 번호';
COMMENT ON COLUMN users.login_id IS '사용자 로그인 ID';
COMMENT ON COLUMN users.password IS '사용자 비밀번호';

CREATE UNIQUE INDEX ix01_users ON users (no);
CREATE UNIQUE INDEX ix02_users ON users (login_id);
