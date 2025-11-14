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
public class NicepayPaymentCancelResponse {

    // 취소 결과 코드 (4 byte, 예: 2001)
    @JsonProperty("ResultCode")
    private String resultCode;

    // 취소 결과 메시지 (100 byte, 예: 취소 성공)
    @JsonProperty("ResultMsg")
    private String resultMsg;

    // 취소 금액 (12 byte, 예: 000000001000 → 1000원)
    @JsonProperty("CancelAmt")
    private String cancelAmt;

    // 가맹점 ID (10 byte, 예: nictest00m)
    @JsonProperty("MID")
    private String mid;

    // 가맹점 주문번호 (64 byte)
    @JsonProperty("Moid")
    private String moid;

    // 위변조 검증 데이터 (hex sha256(TID + MID + CancelAmt + MerchantKey), 500 byte)
    @JsonProperty("Signature")
    private String signature;

    // 결제수단 (CARD/BANK/VBANK/CELLPHONE)
    @JsonProperty("PayMethod")
    private String payMethod;

    // 거래 ID (30 byte)
    @JsonProperty("TID")
    private String tid;

    // 취소일자 (8 byte, YYYYMMDD)
    @JsonProperty("CancelDate")
    private String cancelDate;

    // 취소시간 (6 byte, HHmmss)
    @JsonProperty("CancelTime")
    private String cancelTime;

    // 취소번호 (8 byte)
    @JsonProperty("CancelNum")
    private String cancelNum;

    // 취소 후 잔액 (12 byte, 예: 000000001000 → 1000원)
    @JsonProperty("RemainAmt")
    private String remainAmt;

    // 가맹점 여분 필드 (500 byte)
    @JsonProperty("MallReserved")
    private String mallReserved;
}
