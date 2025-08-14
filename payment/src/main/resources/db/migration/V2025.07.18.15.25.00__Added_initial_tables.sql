USE
`payment`;

CREATE TABLE payments
(
    pay_no varchar(100) NOT NULL comment '결제 번호',
    PRIMARY KEY (pay_no)
) comment '결제';