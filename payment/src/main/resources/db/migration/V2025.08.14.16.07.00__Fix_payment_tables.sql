ALTER TABLE payments
    ADD COLUMN payment_no         VARCHAR(100) NOT NULL COMMENT '결제 번호',
    ADD COLUMN order_no           VARCHAR(100) NOT NULL COMMENT '주문 번호',
    ADD COLUMN pg_type            VARCHAR(50)  NOT NULL COMMENT '결제 대행사 타입',
    ADD COLUMN status             VARCHAR(20)  NOT NULL COMMENT '결제 상태',
    ADD COLUMN amount             BIGINT       NOT NULL COMMENT '결제 금액',
    ADD COLUMN tid                VARCHAR(20) COMMENT '결제 번호',
    ADD COLUMN buyer_phone_number VARCHAR(20) COMMENT '구매자 핸드폰 번호',
    ADD COLUMN buyer_email        VARCHAR(50) COMMENT '구매자 이메일 주소'
;
