CREATE TABLE member (
                       member_id SERIAL PRIMARY KEY,  -- AUTO_INCREMENT 대체
                       member_no VARCHAR(20) UNIQUE,  -- 고유 제약 조건 통합
                       login_id VARCHAR(20) UNIQUE,
                       password VARCHAR(255) NOT NULL
);

COMMENT ON TABLE member IS '유저';
COMMENT ON COLUMN member.member_id IS '사용자고유ID';
COMMENT ON COLUMN member.member_no IS '사용자번호';
COMMENT ON COLUMN member.login_id IS '사용자로그인ID';
COMMENT ON COLUMN member.password IS '비밀번호';

-- 초기 관리자 계정 추가
INSERT INTO member(member_no, login_id, password)
VALUES ('0', 'test', '$2a$10$kMfZrcEBYlI26eIsfbbOQ.A1/WAT25YbVi7/Ure8NxY6FLE0oZfRa');