package io.github.monty.api.payment.infrastructure.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NicepayPaymentCancelRequest {
    // TID: 거래 ID (30 byte)
    private String TID;

    // MID: 가맹점 ID (10 byte)
    private String MID;

    // Moid: 주문번호 (64 byte, 부분 취소 시 중복취소 방지 목적, 별도 계약 필요)
    private String Moid;

    // CancelAmt: 취소금액 (12 byte)
    private String CancelAmt;

    // CancelMsg: 취소사유 (100 byte, euc-kr)
    private String CancelMsg;

    // PartialCancelCode: 0=전체취소, 1=부분취소 (1 byte, 별도 계약 필요)
    private String PartialCancelCode;

    // EdiDate: 전문생성일시 (YYYYMMDDHHMMSS, 14 byte)
    private String EdiDate;

    // SignData: hex(sha256(MID + CancelAmt + EdiDate + MerchantKey)) (256 byte)
    private String SignData;

    // CharSet: 인코딩 타입 (euc-kr(default) / utf-8) (10 byte)
    private String CharSet;

    // EdiType: 응답전문 유형 (JSON / KV)
    private String EdiType;

    // MallReserved: 가맹점 여분 필드 (500 byte)
    private String MallReserved;

    // RefundAcctNo: 환불 계좌번호 (16 byte, 가상계좌/휴대폰 익월 환불 Only)
    private String RefundAcctNo;

    // RefundBankCd: 환불 계좌은행 코드 (3 byte)
    private String RefundBankCd;

    // RefundAcctNm: 환불계좌주명 (10 byte, euc-kr)
    private String RefundAcctNm;
}
