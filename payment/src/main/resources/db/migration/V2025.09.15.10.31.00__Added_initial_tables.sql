-- 결제 테이블
CREATE TABLE payment (
                         payment_id         VARCHAR(100)  NOT NULL,
                         transaction_id     VARCHAR(100),
                         payment_no         VARCHAR(100)  NOT NULL,
                         amount             BIGINT        NOT NULL,
                         order_no           VARCHAR(100)  NOT NULL,
                         pg_provider_type   VARCHAR(50)   NOT NULL,
                         payment_status     VARCHAR(50)   NOT NULL,
                         payment_method     VARCHAR(20),
                         approval_date_time TIMESTAMP,
                         buyer_phone        VARCHAR(20),
                         buyer_email        VARCHAR(50),
                         created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         PRIMARY KEY (payment_id)
);

COMMENT ON TABLE payment IS '결제';
COMMENT ON COLUMN payment.payment_id IS '결제 ID';
COMMENT ON COLUMN payment.transaction_id IS 'PG사 거래 번호';
COMMENT ON COLUMN payment.payment_no IS '결제 번호';
COMMENT ON COLUMN payment.amount IS '금액';
COMMENT ON COLUMN payment.order_no IS '주문 번호';
COMMENT ON COLUMN payment.pg_provider_type IS '결제 대행사 타입';
COMMENT ON COLUMN payment.payment_status IS '결제 상태';
COMMENT ON COLUMN payment.payment_method IS '결제 수단';
COMMENT ON COLUMN payment.approval_date_time IS '결제 승인 일시';
COMMENT ON COLUMN payment.buyer_phone IS '구매자 휴대폰 번호';
COMMENT ON COLUMN payment.buyer_email IS '구매자 이메일 주소';
COMMENT ON COLUMN payment.created_at IS '데이터 생성 시각';
COMMENT ON COLUMN payment.updated_at IS '데이터 수정 시각';


-- 이니시스 결제 테이블
CREATE TABLE payment_inicis (
                                payment_id         VARCHAR(100)  NOT NULL,
                                auth_token         TEXT          NOT NULL,
                                idc_code           VARCHAR(20)   NOT NULL,
                                approval_url       VARCHAR(255)  NOT NULL,
                                network_cancel_url VARCHAR(255)  NOT NULL,
                                created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                PRIMARY KEY (payment_id),
                                FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
);

COMMENT ON TABLE payment_inicis IS '이니시스 결제';
COMMENT ON COLUMN payment_inicis.payment_id IS '결제 ID';
COMMENT ON COLUMN payment_inicis.auth_token IS '승인 요청 검증 토큰';
COMMENT ON COLUMN payment_inicis.idc_code IS 'IDC 센터 코드';
COMMENT ON COLUMN payment_inicis.approval_url IS '승인 요청 URL';
COMMENT ON COLUMN payment_inicis.network_cancel_url IS '망취소 요청 URL';
COMMENT ON COLUMN payment_inicis.created_at IS '데이터 생성 시각';
COMMENT ON COLUMN payment_inicis.updated_at IS '데이터 수정 시각';


-- 결제 취소 테이블
CREATE TABLE payment_cancel (
                                payment_cancel_id     BIGINT GENERATED ALWAYS AS IDENTITY,
                                payment_id            VARCHAR(100)  NOT NULL,
                                cancel_transaction_id VARCHAR(100),
                                payment_cancel_type   VARCHAR(50)   NOT NULL,
                                cancel_amount         BIGINT        NOT NULL,
                                reason                VARCHAR(255),
                                created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                PRIMARY KEY (payment_cancel_id),
                                FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
);

COMMENT ON TABLE payment_cancel IS '결제 취소';
COMMENT ON COLUMN payment_cancel.payment_cancel_id IS '결제 취소 ID';
COMMENT ON COLUMN payment_cancel.payment_id IS '결제 ID';
COMMENT ON COLUMN payment_cancel.cancel_transaction_id IS '취소 PG 거래 번호';
COMMENT ON COLUMN payment_cancel.payment_cancel_type IS '결제 취소 타입';
COMMENT ON COLUMN payment_cancel.cancel_amount IS '취소 금액';
COMMENT ON COLUMN payment_cancel.reason IS '취소 사유';
COMMENT ON COLUMN payment_cancel.created_at IS '데이터 생성 시각';
COMMENT ON COLUMN payment_cancel.updated_at IS '데이터 수정 시각';


-- 결제 로그 테이블
CREATE TABLE payment_log (
                             payment_log_id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
                             payment_id     VARCHAR(100) NOT NULL,
                             payment_status VARCHAR(50)  NOT NULL,
                             message        TEXT,
                             created_at     TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             updated_at     TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             PRIMARY KEY (payment_log_id),
                             FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
);

COMMENT ON TABLE payment_log IS '결제 로그';
COMMENT ON COLUMN payment_log.payment_log_id IS '로그 ID';
COMMENT ON COLUMN payment_log.payment_id IS '결제 ID';
COMMENT ON COLUMN payment_log.payment_status IS '결제 상태';
COMMENT ON COLUMN payment_log.message IS '메시지';
COMMENT ON COLUMN payment_log.created_at IS '데이터 생성 시각';
COMMENT ON COLUMN payment_log.updated_at IS '데이터 수정 시각';