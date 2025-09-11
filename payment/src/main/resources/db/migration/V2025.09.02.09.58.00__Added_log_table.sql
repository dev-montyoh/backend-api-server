CREATE TABLE payment_log
(
    payment_log_id bigint AUTO_INCREMENT NOT NULL COMMENT '로그 ID',
    payment_id     varchar(100)                        NOT NULL COMMENT '결제 ID',
    payment_status varchar(20)                         NOT NULL COMMENT '결제 상태',
    message        text NULL COMMENT '메시지',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 생성 시각',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '데이터 수정 시각',
    PRIMARY KEY (payment_log_id),
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '결제 로그';
