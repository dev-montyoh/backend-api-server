-- 인증 스키마 생성 및 사용
CREATE SCHEMA IF NOT EXISTS auth;
SET search_path TO auth;

-- 역할 테이블
CREATE TABLE role
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name        VARCHAR(20)                         NOT NULL,
    description VARCHAR(20)                         NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE role IS '역할';
COMMENT ON COLUMN role.id IS '역할 고유 ID';
COMMENT ON COLUMN role.name IS '역할 이름';
COMMENT ON COLUMN role.description IS '역할 설명';

CREATE UNIQUE INDEX ix01_role ON role (name);

-- 사용자 역할 테이블
CREATE TABLE user_role
(
    user_no VARCHAR(20) NOT NULL,
    role_id BIGINT      NOT NULL,
    PRIMARY KEY (user_no, role_id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

COMMENT ON TABLE user_role IS '사용자 역할';
COMMENT ON COLUMN user_role.user_no IS '사용자 번호';
COMMENT ON COLUMN user_role.role_id IS '역할 고유 ID';

-- 기본 데이터
INSERT INTO role(name, description) VALUES ('ROLE_GUEST', '방문자');
INSERT INTO role(name, description) VALUES ('ROLE_MASTER', '마스터');

INSERT INTO user_role(user_no, role_id) VALUES ('0', 2);
