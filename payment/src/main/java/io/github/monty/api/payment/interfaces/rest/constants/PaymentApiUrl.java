package io.github.monty.api.payment.interfaces.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PaymentApiUrl {

    public static final String PAYMENT_BASE_URL = "/payment";
    public static final String PAYMENT_VERSION = "/v1";
    public static final String PAYMENT_URL = PAYMENT_BASE_URL + PAYMENT_VERSION;

    @UtilityClass
    public static final class Payment {
        public static final String PAYMENT_LIST_URL = "/payments";
        public static final String PAYMENT_APPROVAL_URL = "/payments/{paymentNo}/approval";
        public static final String PAYMENT_CANCEL_URL = "/payments/{paymentNo}/cancel";
        public static final String PAYMENT_NET_CANCEL_URL = "/payments/{paymentNo}/network-cancel";
    }

    @UtilityClass
    public static final class Inicis {
        public static final String INICIS_URL = "/payments/inicis";
        public static final String INICIS_SIGNATURE_URL = "/payments/inicis/signature";
    }
}
