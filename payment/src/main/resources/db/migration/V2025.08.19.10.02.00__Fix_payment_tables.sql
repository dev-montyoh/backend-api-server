CREATE TABLE inicis_payments
(
    payment_id         VARCHAR(100) NOT NULL COMMENT '결제 ID' PRIMARY KEY,
    auth_token         TEXT         NOT NULL COMMENT '승인요청 검증 토큰',
    idc_name           VARCHAR(20)  NOT NULL COMMENT 'IDC 센터 코드',
    auth_url           VARCHAR(255) NOT NULL COMMENT '승인요청 URL',
    net_cancel_url     VARCHAR(255) NOT NULL COMMENT '망취소요청 URL',
    payment_method     VARCHAR(20) COMMENT '지불 수단',
    approval_date_time TIMESTAMP COMMENT '결제 승인 일시',
    FOREIGN KEY (payment_id) REFERENCES payments (payment_id)
);