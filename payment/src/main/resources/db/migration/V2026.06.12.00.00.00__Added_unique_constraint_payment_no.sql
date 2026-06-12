ALTER TABLE payment ADD CONSTRAINT uq_payment_no UNIQUE (payment_no);

COMMENT ON CONSTRAINT uq_payment_no ON payment IS '결제 번호 유니크 제약';
