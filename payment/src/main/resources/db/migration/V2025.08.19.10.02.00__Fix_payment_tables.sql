ALTER TABLE payments
    ADD COLUMN payment_type   VARCHAR(50)  NOT NULL COMMENT '결제 수단',
    ADD COLUMN payment_no     VARCHAR(100) NOT NULL COMMENT '결제 번호',
    ADD COLUMN payment_status VARCHAR(20)  NOT NULL COMMENT '결제 상태'
;

CREATE TABLE inicis_payments
(
    payment_id     VARCHAR(100) NOT NULL COMMENT '결제 ID' PRIMARY KEY,
    auth_token     TEXT         NOT NULL COMMENT '승인요청 검증 토큰',
    idc_name       VARCHAR(20)  NOT NULL COMMENT 'IDC 센터 코드',
    auth_url       VARCHAR(255) NOT NULL COMMENT '승인요청 URL',
    net_cancel_url VARCHAR(255) NOT NULL COMMENT '망취소요청 URL',
    FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
);