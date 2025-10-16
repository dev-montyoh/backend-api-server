-- 1️⃣ content 테이블 생성
CREATE TABLE content
(
    content_id BIGSERIAL PRIMARY KEY
);

COMMENT ON TABLE content IS '컨텐츠';
COMMENT ON COLUMN content.content_id IS '컨텐츠 ID';


-- 2️⃣ content_album 테이블 생성
CREATE TABLE content_album
(
    content_id  BIGINT       NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT         NOT NULL,
    PRIMARY KEY (content_id),
    FOREIGN KEY (content_id) REFERENCES content (content_id) ON DELETE CASCADE
);

COMMENT ON TABLE content_album IS '앨범 컨텐츠';
COMMENT ON COLUMN content_album.content_id IS '컨텐츠 ID';
COMMENT ON COLUMN content_album.image_url IS '이미지 경로';
COMMENT ON COLUMN content_album.title IS '컨텐츠 이름';
COMMENT ON COLUMN content_album.description IS '컨텐츠 설명';


-- 3️⃣ tag 테이블 생성
CREATE TABLE tag
(
    tag_id   BIGSERIAL PRIMARY KEY,
    tag_name VARCHAR(100) NOT NULL UNIQUE
);

COMMENT ON TABLE tag IS '태그 정보';
COMMENT ON COLUMN tag.tag_id IS '태그 ID';
COMMENT ON COLUMN tag.tag_name IS '태그 이름';


-- 4️⃣ content_tag 테이블 생성
CREATE TABLE content_tag
(
    content_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (content_id, tag_id),
    FOREIGN KEY (content_id) REFERENCES content (content_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag (tag_id) ON DELETE CASCADE
);

COMMENT ON TABLE content_tag IS 'content - tag 연결 테이블';
COMMENT ON COLUMN content_tag.content_id IS '콘텐츠 번호(외부키)';
COMMENT ON COLUMN content_tag.tag_id IS '태그 ID(외부키)';


-- 초기 데이터 삽입
-- tag 데이터
INSERT INTO tag (tag_id, tag_name)
VALUES (1, 'TAG1'),
       (2, 'TAG2'),
       (3, 'TAG3');

-- content 데이터
INSERT INTO content (content_id)
VALUES (1),
       (2),
       (3),
       (4),
       (5);

-- content_album 데이터
INSERT INTO content_album (content_id, image_url, title, description)
VALUES (1, '/content/v1/public/images/cat_1.jpeg', '떼껄룩1', '떼껄룩1'),
       (2, '/content/v1/public/images/cat_2.jpeg', '떼껄룩2', '떼껄룩2'),
       (3, '/content/v1/public/images/cat_3.jpeg', '떼껄룩3', '떼껄룩3'),
       (4, '/content/v1/public/images/cat_4.jpeg', '떼껄룩4', '떼껄룩4'),
       (5, '/content/v1/public/images/cat_5.jpeg', '떼껄룩5', '떼껄룩5');

-- content_tag 데이터
INSERT INTO content_tag (content_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 3),
       (5, 3);