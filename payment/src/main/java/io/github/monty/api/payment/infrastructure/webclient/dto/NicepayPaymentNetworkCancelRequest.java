package io.github.monty.api.payment.infrastructure.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NicepayPaymentNetworkCancelRequest {
    /** 거래번호 (인증 응답 TxTid 사용) */
    private String TID;

    /** 인증 TOKEN */
    private String AuthToken;

    /** 가맹점 아이디 (예: nicepay00m) */
    private String MID;

    /** 금액 (숫자만) */
    private String Amt;

    /** 전문생성일시 (YYYYMMDDHHMMSS) */
    private String EdiDate;

    /** hex(sha256(AuthToken + MID + Amt + EdiDate + MerchantKey)) */
    private String SignData;

    /** 응답전문 유형 (JSON / KV) *KV:Key=value **/
    private String EdiType;

    /** 인증 응답 인코딩 (euc-kr(default) / utf-8) **/
    private String CharSet;

    /** 가맹점 여분 필드 **/
    private String MallReserved;

    /** 망취소 여부 (1 고정) **/
    private String NetCancel;
}
