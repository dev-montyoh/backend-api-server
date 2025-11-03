package io.github.monty.api.payment.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InicisPaymentCancelReqVo {

    private String mid;

    private String type;

    private String timestamp;

    private String clientIp;

    private String hashData;

    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private String tid;

        private String msg;
    }
}

