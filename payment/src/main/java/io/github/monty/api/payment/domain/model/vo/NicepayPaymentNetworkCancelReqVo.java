package io.github.monty.api.payment.domain.model.vo;

import io.github.monty.api.payment.domain.model.aggregate.NicepayPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public class NicepayPaymentNetworkCancelReqVo {

    public NicepayPaymentNetworkCancelReqVo(NicepayPayment nicepayPayment, String mid, String ediDate, String signData) {
        this.transactionId = nicepayPayment.getTransactionId();
        this.authToken = nicepayPayment.getAuthToken();
        this.mid = mid;
        this.amount = nicepayPayment.getAmount();
        this.ediDate = ediDate;
        this.signData = signData;
        this.MallReserved = StringUtils.EMPTY;
        this.networkCancelUrl = nicepayPayment.getNetworkCancelUrl();
        this.orderNo = nicepayPayment.getOrderNo();
    }

    private String transactionId;

    private String authToken;

    private String mid;

    private long amount;

    private String ediDate;

    private String signData;

    private String MallReserved;

    private String networkCancelUrl;

    private String orderNo;
}
