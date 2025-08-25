-- 테이블명 변경으로 인한 DROP 처리
DROP TABLE inicis_payments;
DROP TABLE payments;

-- 신규 테이블 등록
CREATE TABLE payment
(
    payment_id       varchar(100) NOT NULL COMMENT '결제 ID',
    payment_no       varchar(100) NOT NULL COMMENT '결제 번호',
    order_no         varchar(100) NOT NULL COMMENT '주문 번호',
    pg_provider_type varchar(50)  NOT NULL COMMENT '결제 대행사 타입',
    payment_status   varchar(20)  NOT NULL COMMENT '결제 상태',
    request_amount   bigint       NOT NULL COMMENT '요청 금액',
    approval_amount  bigint       NOT NULL COMMENT '승인 금액',
    refund_amount    bigint       NOT NULL COMMENT '환불 금액',
    transaction_id   varchar(100) NULL COMMENT 'PG사 거래 번호',
    buyer_phone      varchar(20)  NULL COMMENT '구매자 휴대폰 번호',
    buyer_email      varchar(50)  NULL COMMENT '구매자 이메일 주소',
    PRIMARY KEY (payment_id)
) COMMENT '결제';

CREATE TABLE payment_inicis
(
    payment_id         varchar(100) NOT NULL COMMENT '결제 ID',
    auth_token         text         NOT NULL COMMENT '승인 요청 검증 토큰',
    idc_code           varchar(20)  NOT NULL COMMENT 'IDC 센터 코드',
    approval_url       varchar(255) NOT NULL COMMENT '승인 요청 URL',
    cancel_url         varchar(255) NOT NULL COMMENT '망취소 요청 URL',
    payment_method     varchar(20)  NULL COMMENT '결제 수단',
    approval_date_time timestamp    NULL COMMENT '결제 승인 일시',
    PRIMARY KEY (payment_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '이니시스 결제';
