package io.github.monty.api.payment.infrastructure.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiUrl {
    public static final String INICIS_PAYMENT_CANCEL_URL = "https://iniapi.inicis.com/v2/pg/refund";

    public static final String NICEPAY_PAYMENT_CANCEL_URL = "https://pg-api.nicepay.co.kr/webapi/cancel_process.jsp";
}
