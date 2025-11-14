--  나이스페이 결제 테이블
CREATE TABLE payment_nicepay
(
    payment_id         VARCHAR(100)                        NOT NULL,
    auth_token         TEXT                                NOT NULL,
    signature          TEXT                                NOT NULL,
    next_approval_url  VARCHAR(255)                        NOT NULL,
    network_cancel_url VARCHAR(255)                        NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (payment_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
);

COMMENT ON TABLE payment_nicepay IS '나이스페이 결제';
COMMENT ON COLUMN payment_nicepay.payment_id IS '결제 ID';
COMMENT ON COLUMN payment_nicepay.auth_token IS '승인 요청 검증 토큰';
COMMENT ON COLUMN payment_nicepay.signature IS '위변조 검증 데이터';
COMMENT ON COLUMN payment_nicepay.next_approval_url IS '승인 요청 URL';
COMMENT ON COLUMN payment_nicepay.network_cancel_url IS '망취소 요청 URL';
COMMENT ON COLUMN payment_nicepay.created_at IS '데이터 생성 시각';
COMMENT ON COLUMN payment_nicepay.updated_at IS '데이터 수정 시각';