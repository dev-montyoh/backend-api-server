CREATE TABLE roles (
                       id          SERIAL PRIMARY KEY,         -- 역할 고유 ID
                       name        VARCHAR(20) NOT NULL UNIQUE, -- 역할 이름 (고유)
                       description VARCHAR(20) NOT NULL         -- 역할 설명
);

COMMENT ON TABLE roles IS '역할';
COMMENT ON COLUMN roles.id IS '역할 고유 ID';
COMMENT ON COLUMN roles.name IS '역할 이름';
COMMENT ON COLUMN roles.description IS '역할 설명';

INSERT INTO roles(name, description) VALUES
                                         ('ROLE_GUEST', '방문자'),
                                         ('ROLE_MASTER', '마스터');

CREATE TABLE user_roles (
                            user_no VARCHAR(20) NOT NULL,       -- 사용자번호
                            role_id INT NOT NULL,               -- 역할 고유 ID
                            PRIMARY KEY (user_no, role_id),
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

COMMENT ON TABLE user_roles IS '사용자 역할 관계';
COMMENT ON COLUMN user_roles.user_no IS '사용자번호';
COMMENT ON COLUMN user_roles.role_id IS '역할 고유 ID';

INSERT INTO user_roles(user_no, role_id) VALUES ('0', 2);
