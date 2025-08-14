package io.github.monty.api.payment.domain.model.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InisysPaymentCreateCommand extends PaymentCreateCommand {
    private String returnUrl;

    private String cp_yn;

    private String charset;

    private String orderNumber;

    private String authToken;

    private String checkAckUrl;

    private String netCancelUrl;

    private String mid;

    private String idc_name;

    private String merchantData;

    private String authUrl;

    private String cardnum;

    private String cardUsePoint;
}
