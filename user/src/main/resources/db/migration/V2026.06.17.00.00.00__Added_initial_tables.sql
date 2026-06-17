CREATE SCHEMA IF NOT EXISTS "user";
SET search_path TO "user";

CREATE TABLE users
(
    USER_ID       VARCHAR(100)                 NOT NULL,
    USER_NO       VARCHAR(100)                 NOT NULL,
    USER_LOGIN_ID VARCHAR(100)                 NOT NULL,
    USER_PASSWORD VARCHAR(255)                 NOT NULL,
    PRIMARY KEY (USER_ID)
);

COMMENT ON TABLE users IS '유저';
COMMENT ON COLUMN users.USER_ID IS '사용자 고유 ID';
COMMENT ON COLUMN users.USER_NO IS '사용자 번호';
COMMENT ON COLUMN users.USER_LOGIN_ID IS '사용자 로그인 ID';
COMMENT ON COLUMN users.USER_PASSWORD IS '사용자 비밀번호';

CREATE UNIQUE INDEX ix01_users ON users (USER_NO);
CREATE UNIQUE INDEX ix02_users ON users (USER_LOGIN_ID);
