ALTER TABLE payment_cancel
    DROP COLUMN payment_cancel_id;

ALTER TABLE payment_cancel
    ADD COLUMN payment_cancel_id VARCHAR(100) NOT NULL DEFAULT gen_random_uuid()::VARCHAR;

ALTER TABLE payment_cancel
    ADD CONSTRAINT pk_payment_cancel PRIMARY KEY (payment_cancel_id);

ALTER TABLE payment_cancel
    ALTER COLUMN payment_cancel_id DROP DEFAULT;

COMMENT ON COLUMN payment_cancel.payment_cancel_id IS '결제 취소 ID';
