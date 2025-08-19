USE
`payment`;

CREATE TABLE payments
(
    payment_id varchar(100) NOT NULL comment '결제 ID',
    PRIMARY KEY (payment_id)
) comment '결제';