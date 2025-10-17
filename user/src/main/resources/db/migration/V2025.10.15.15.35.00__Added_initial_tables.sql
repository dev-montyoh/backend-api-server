-- 회원 테이블 추가
CREATE TABLE member
(
    member_id SERIAL PRIMARY KEY, -- AUTO_INCREMENT 대체
    member_no VARCHAR(20) UNIQUE, -- 고유 제약 조건 통합
    login_id  VARCHAR(20) UNIQUE,
    password  VARCHAR(255) NOT NULL
);

COMMENT ON TABLE member IS '회원';
COMMENT ON COLUMN member.member_id IS '사용자고유ID';
COMMENT ON COLUMN member.member_no IS '사용자번호';
COMMENT ON COLUMN member.login_id IS '사용자로그인ID';
COMMENT ON COLUMN member.password IS '비밀번호';

-- 초기 관리자 계정 추가
INSERT INTO member(member_no, login_id, password)
VALUES ('0', 'test', '$2a$10$kMfZrcEBYlI26eIsfbbOQ.A1/WAT25YbVi7/Ure8NxY6FLE0oZfRa');

-- 역할 테이블 추가
CREATE TABLE role
(
    role_id     SERIAL PRIMARY KEY,          -- 역할 고유 ID
    name        VARCHAR(20) NOT NULL UNIQUE, -- 역할 이름 (고유)
    description VARCHAR(20) NOT NULL         -- 역할 설명
);

COMMENT ON TABLE role IS '역할';
COMMENT ON COLUMN role.role_id IS '역할 고유 ID';
COMMENT ON COLUMN role.name IS '역할 이름';
COMMENT ON COLUMN role.description IS '역할 설명';

INSERT INTO role(name, description)
VALUES ('ROLE_GUEST', '방문자'),
       ('ROLE_MASTER', '마스터');

-- 회원 역할 연결 테이블
CREATE TABLE member_role
(
    member_no VARCHAR(20) NOT NULL, -- 사용자번호
    role_id   INT         NOT NULL, -- 역할 고유 ID
    PRIMARY KEY (member_no, role_id),
    FOREIGN KEY (role_id) REFERENCES role (role_id),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);

COMMENT ON TABLE member_role IS '사용자 역할 관계';
COMMENT ON COLUMN member_role.member_no IS '사용자번호';
COMMENT ON COLUMN member_role.role_id IS '역할 고유 ID';

INSERT INTO member_role(member_no, role_id)
VALUES ('0', 2);
