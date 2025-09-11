ALTER TABLE payment_inicis RENAME COLUMN cancel_url TO network_cancel_url;
ALTER TABLE payment_inicis DROP COLUMN approval_date_time