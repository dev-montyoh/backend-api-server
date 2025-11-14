package io.github.monty.api.payment.domain.model.vo;

import io.github.monty.api.payment.domain.model.aggregate.NicepayPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public class NicepayPaymentCancelReqVo {

    public NicepayPaymentCancelReqVo(NicepayPayment nicepayPayment, String mid, String ediDate, String signData, String cancelReason) {
        this.transactionId = nicepayPayment.getTransactionId();
        this.mid = mid;
        this.orderNo = nicepayPayment.getOrderNo();
        this.cancelAmount = nicepayPayment.getAmount();
        this.cancelReason = cancelReason;
        this.ediDate = ediDate;
        this.signData = signData;
        this.mallReserved = StringUtils.EMPTY;
    }

    private String transactionId;

    private String mid;

    private String orderNo;

    private long cancelAmount;

    private String cancelReason;

    private String ediDate;

    private String signData;

    private String mallReserved;
}
