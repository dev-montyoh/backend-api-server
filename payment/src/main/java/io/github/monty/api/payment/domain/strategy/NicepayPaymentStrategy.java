package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.NicepayPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicepayPaymentStrategy implements PaymentStrategy {

    private static final String NICEPAY_SIGN_DATA_MESSAGE_FORMAT = "{0}{1}{2}{3}";

    private static final String NICEPAY_EDIDATE_FORMAT = "yyyyMMddHHmmss";

    @Value("payment.type.nicepay.mid")
    private String nicepayMid;

    @Value("payment.type.nicepay.merchant.key")
    private String nicepayMerchantKey;

    @Override
    public PaymentServiceProviderType getPaymentType() {return PaymentServiceProviderType.NICEPAY;}

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    @Override
    public PaymentSignatureResultVO getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        NicepayPaymentSignatureQuery nicepayPaymentSignatureQuery = (NicepayPaymentSignatureQuery) paymentSignatureQuery;
        LocalDateTime localDateTime = LocalDateTime.now();
        String ediDate = localDateTime.format(DateTimeFormatter.ofPattern(NICEPAY_EDIDATE_FORMAT));
        String plainTextSignData = String.format(NICEPAY_SIGN_DATA_MESSAGE_FORMAT, ediDate, nicepayMid, nicepayPaymentSignatureQuery.getPrice(), nicepayMerchantKey);

        String signData = EncryptUtils.encrypt(plainTextSignData, EncryptType.SHA256);

        return null;
    }

    @Override
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {
        return null;
    }

    @Override
    public void approvePayment(String paymentNo) {

    }
}
