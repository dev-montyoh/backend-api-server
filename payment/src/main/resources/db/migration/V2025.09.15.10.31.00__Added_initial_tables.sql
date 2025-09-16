-- 결제 테이블
CREATE TABLE payment
(
    payment_id         VARCHAR(100)                        NOT NULL COMMENT '결제 ID',
    transaction_id     VARCHAR(100) NULL COMMENT 'PG사 거래 번호',
    payment_no         VARCHAR(100)                        NOT NULL COMMENT '결제 번호',
    amount             BIGINT                              NOT NULL COMMENT '금액',
    order_no           VARCHAR(100)                        NOT NULL COMMENT '주문 번호',
    pg_provider_type   VARCHAR(50)                         NOT NULL COMMENT '결제 대행사 타입',
    payment_status     VARCHAR(50)                         NOT NULL COMMENT '결제 상태',
    payment_method     VARCHAR(20) NULL COMMENT '결제 수단',
    approval_date_time TIMESTAMP NULL COMMENT '결제 승인 일시',
    buyer_phone        VARCHAR(20) NULL COMMENT '구매자 휴대폰 번호',
    buyer_email        VARCHAR(50) NULL COMMENT '구매자 이메일 주소',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 생성 시각',
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 수정 시각',
    PRIMARY KEY (payment_id)
) COMMENT '결제';

-- 이니시스 결제 테이블
CREATE TABLE payment_inicis
(
    payment_id         VARCHAR(100)                        NOT NULL COMMENT '결제 ID',
    auth_token         TEXT                                NOT NULL COMMENT '승인 요청 검증 토큰',
    idc_code           VARCHAR(20)                         NOT NULL COMMENT 'IDC 센터 코드',
    approval_url       VARCHAR(255)                        NOT NULL COMMENT '승인 요청 URL',
    network_cancel_url VARCHAR(255)                        NOT NULL COMMENT '망취소 요청 URL',
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 생성 시각',
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 수정 시각',
    PRIMARY KEY (payment_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '이니시스 결제';

-- 결제 취소 테이블
CREATE TABLE payment_cancel
(
    payment_cancel_id     BIGINT AUTO_INCREMENT COMMENT '결제 취소 ID',
    payment_id            VARCHAR(100)                        NOT NULL COMMENT '결제 ID',
    cancel_transaction_id VARCHAR(100) NULL COMMENT '취소 PG 거래 번호',
    payment_cancel_type   VARCHAR(50)                         NOT NULL COMMENT '결제 취소 타입',
    cancel_amount         BIGINT                              NOT NULL COMMENT '취소 금액',
    reason                VARCHAR(255) NULL COMMENT '취소 사유',
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 생성 시각',
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 수정 시각',
    PRIMARY KEY (payment_cancel_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '결제 취소';

-- 결제 로그 테이블
CREATE TABLE payment_log
(
    payment_log_id bigint AUTO_INCREMENT NOT NULL COMMENT '로그 ID',
    payment_id     varchar(100)                        NOT NULL COMMENT '결제 ID',
    payment_status varchar(50)                         NOT NULL COMMENT '결제 상태',
    message        text NULL COMMENT '메시지',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 생성 시각',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 수정 시각',
    PRIMARY KEY (payment_log_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '결제 로그';
