package io.github.monty.api.payment.domain.model.vo;

import io.github.monty.api.payment.domain.model.aggregate.NicepayPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@AllArgsConstructor
public class NicepayPaymentApprovalReqVo {

    public NicepayPaymentApprovalReqVo(NicepayPayment nicepayPayment, String mid, String ediDate, String signData) {
        this.transactionId = nicepayPayment.getTransactionId();
        this.authToken = nicepayPayment.getAuthToken();
        this.mid = mid;
        this.amount = String.valueOf(nicepayPayment.getAmount());
        this.ediDate = ediDate;
        this.signData = signData;
        this.nextApprovalUrl = nicepayPayment.getNextApprovalUrl();
        this.mallReserved = StringUtils.EMPTY;
    }

    /** 거래번호 (인증 응답 TxTid 사용) */
    private String transactionId;

    /** 인증 TOKEN */
    private String authToken;

    /** 가맹점 아이디 (예: nicepay00m) */
    private String mid;

    /** 금액 (숫자만) */
    private String amount;

    /** 전문생성일시 (YYYYMMDDHHMMSS) */
    private String ediDate;

    /** hex(sha256(AuthToken + MID + Amt + EdiDate + MerchantKey)) */
    private String signData;

    private String mallReserved;

    private String nextApprovalUrl;
}
