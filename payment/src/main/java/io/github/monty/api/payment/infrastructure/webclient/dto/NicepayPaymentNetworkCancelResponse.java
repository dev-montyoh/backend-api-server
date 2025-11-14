package io.github.monty.api.payment.infrastructure.webclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NicepayPaymentNetworkCancelResponse {
    // 취소 결과 코드 (예: 2001)
    @JsonProperty("ResultCode")
    private String resultCode;

    // 취소 결과 메시지 (예: 취소 성공)
    @JsonProperty("ResultMsg")
    private String resultMsg;

    // 취소 금액 (예: 000000001000 → 1000원)
    @JsonProperty("CancelAmt")
    private String cancelAmt;

    // 가맹점 ID (예: nictest00m)
    @JsonProperty("MID")
    private String mid;

    // 가맹점 주문번호
    @JsonProperty("Moid")
    private String moid;

    // 위변조 검증용 서명 데이터 (TID + MID + CancelAmt + MerchantKey)
    @JsonProperty("Signature")
    private String signature;

    // 결제 수단 (CARD, BANK, VBANK, CELLPHONE 등)
    @JsonProperty("PayMethod")
    private String payMethod;

    // 거래 ID
    @JsonProperty("TID")
    private String tid;

    // 취소일자 (YYYYMMDD)
    @JsonProperty("CancelDate")
    private String cancelDate;

    // 취소시간 (HHmmss)
    @JsonProperty("CancelTime")
    private String cancelTime;

    // 취소번호
    @JsonProperty("CancelNum")
    private String cancelNum;

    // 취소 후 잔액 (예: 000000001000 → 1000원)
    @JsonProperty("RemainAmt")
    private String remainAmt;

    // 가맹점 여분 필드
    @JsonProperty("MallReserved")
    private String mallReserved;
}
