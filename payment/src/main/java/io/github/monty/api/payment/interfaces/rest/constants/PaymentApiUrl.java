package io.github.monty.api.payment.interfaces.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PaymentApiUrl {

    public static final String PAYMENT_BASE_URL = "/payment";
    public static final String PAYMENT_VERSION = "/v1";
    public static final String PAYMENT_URL = PAYMENT_BASE_URL + PAYMENT_VERSION;

    @UtilityClass
    public static final class Inisys {
        public static final String AUTH_INFO_URL = "/inisys/auth/info";
    }
}
