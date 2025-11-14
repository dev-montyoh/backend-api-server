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
public class NicepayPaymentApprovalResponse {

    // ===== 공통 =====
    // 결과 코드
    @JsonProperty("ResultCode")
    private String resultCode;

    // 결과 메시지
    @JsonProperty("ResultMsg")
    private String resultMsg;

    // 결제 금액
    @JsonProperty("Amt")
    private String amt;

    // 가맹점 ID
    @JsonProperty("MID")
    private String mid;

    // 가맹점 주문번호
    @JsonProperty("Moid")
    private String moid;

    // 위변조 검증 데이터 (sha256(TID + MID + Amt + MerchantKey))
    @JsonProperty("Signature")
    private String signature;

    // 구매자 이메일
    @JsonProperty("BuyerEmail")
    private String buyerEmail;

    // 구매자 연락처
    @JsonProperty("BuyerTel")
    private String buyerTel;

    // 구매자명
    @JsonProperty("BuyerName")
    private String buyerName;

    // 상품명
    @JsonProperty("GoodsName")
    private String goodsName;

    // 거래 ID
    @JsonProperty("TID")
    private String tid;

    // 승인 번호
    @JsonProperty("AuthCode")
    private String authCode;

    // 승인 일시 (YYMMDDHHMMSS)
    @JsonProperty("AuthDate")
    private String authDate;

    // 결제 수단 (CARD, BANK, VBANK, CELLPHONE 등)
    @JsonProperty("PayMethod")
    private String payMethod;

    // 가맹점 여분 필드
    @JsonProperty("MallReserved")
    private String mallReserved;


    // ===== 카드 결제 =====
    // 결제 카드사 코드
    @JsonProperty("CardCode")
    private String cardCode;

    // 결제 카드사 이름
    @JsonProperty("CardName")
    private String cardName;

    // 카드번호
    @JsonProperty("CardNo")
    private String cardNo;

    // 할부 개월
    @JsonProperty("CardQuota")
    private String cardQuota;

    // 무이자 적용 여부 (0:미적용, 1:적용)
    @JsonProperty("CardInterest")
    private String cardInterest;

    // 매입 카드사 코드
    @JsonProperty("AcquCardCode")
    private String acquCardCode;

    // 매입 카드사 이름
    @JsonProperty("AcquCardName")
    private String acquCardName;

    // 카드 구분 (0:신용, 1:체크)
    @JsonProperty("CardCl")
    private String cardCl;

    // 부분취소 가능 여부 (0:불가능, 1:가능)
    @JsonProperty("CcPartCl")
    private String ccPartCl;

    // 카드 형태 (01:개인, 02:법인, 03:해외)
    @JsonProperty("CardType")
    private String cardType;

    // 간편결제 구분 (예: 16=KAKAOPAY, 20=NAVERPAY 등)
    @JsonProperty("ClickpayCl")
    private String clickpayCl;

    // 쿠폰 금액
    @JsonProperty("CouponAmt")
    private String couponAmt;

    // 쿠폰 최소 금액
    @JsonProperty("CouponMinAmt")
    private String couponMinAmt;

    // 포인트 승인 금액
    @JsonProperty("PointAppAmt")
    private String pointAppAmt;

    // 복합결제 사용 여부 (0:미사용, 1:사용)
    @JsonProperty("MultiCl")
    private String multiCl;

    // 복합결제 신용카드 금액
    @JsonProperty("MultiCardAcquAmt")
    private String multiCardAcquAmt;

    // 복합결제 포인트 금액
    @JsonProperty("MultiPointAmt")
    private String multiPointAmt;

    // 복합결제 쿠폰 금액
    @JsonProperty("MultiCouponAmt")
    private String multiCouponAmt;

    // 페이코 머니 거래건 현금영수증 발급 대상 금액
    @JsonProperty("MultiRcptAmt")
    private String multiRcptAmt;

    // 네이버페이 포인트 결제 현금영수증 타입
    @JsonProperty("RcptType")
    private String rcptType;

    // 네이버페이 포인트 결제 현금영수증 TID
    @JsonProperty("RcptTID")
    private String rcptTid;

    // 네이버페이 포인트 결제 현금영수증 승인번호
    @JsonProperty("RcptAuthCode")
    private String rcptAuthCode;


    // ===== 가상계좌 =====
    // 결제은행 코드
    @JsonProperty("VbankBankCode")
    private String vbankBankCode;

    // 결제은행명
    @JsonProperty("VbankBankName")
    private String vbankBankName;

    // 가상계좌 번호
    @JsonProperty("VbankNum")
    private String vbankNum;

    // 가상계좌 입금 만료일 (yyyyMMdd)
    @JsonProperty("VbankExpDate")
    private String vbankExpDate;

    // 가상계좌 입금 만료시간 (HHmmss)
    @JsonProperty("VbankExpTime")
    private String vbankExpTime;


    // ===== 계좌이체 =====
    // 결제은행 코드
    @JsonProperty("BankCode")
    private String bankCode;

    // 결제은행명
    @JsonProperty("BankName")
    private String bankName;

    // 현금영수증 타입 (0:발행안함, 1:소득공제, 2:지출증빙)
    @JsonProperty("BankRcptType")
    private String bankRcptType;

    // 현금영수증 TID
    @JsonProperty("BankRcptTID")
    private String bankRcptTid;

    // 현금영수증 승인번호
    @JsonProperty("BankRcptAuthCode")
    private String bankRcptAuthCode;
}
