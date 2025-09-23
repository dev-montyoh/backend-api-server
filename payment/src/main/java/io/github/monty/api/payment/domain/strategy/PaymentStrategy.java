package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResVo;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResVo;

import java.math.BigInteger;
import java.util.UUID;

public interface PaymentStrategy {

    //  기본 결제 번호 Prefix
    String PAYMENT_NO_PREFIX = "PAY";

    /**
     * 해당 결제 수단의 결제번호 생성 후 반환
     *
     * @param PaymentServiceProviderType 결제 타입
     * @return 결제 번호
     */
    default String generatePaymentNo(PaymentServiceProviderType PaymentServiceProviderType) {
        UUID uuid = UUID.randomUUID();
        String base62 = new BigInteger(uuid.toString().replace("-", ""), 16).toString(36);
        return PAYMENT_NO_PREFIX + PaymentServiceProviderType.getCode() + base62;
    }

    /**
     * 해당 전략의 결제 타입을 반환한다.
     *
     * @return 결제 타입
     */
    PaymentServiceProviderType getPaymentType();

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    PaymentSignatureResVo getSignature(PaymentSignatureQuery paymentSignatureQuery);

    /**
     * 해당 결제 정보를 바탕으로 결제 데이터를 저장한다.
     *
     * @param paymentCreateCommand 결제 인증 정보 저장 요청 Command
     * @return 저장 결과
     */
    PaymentCreateResVo createPayment(PaymentCreateCommand paymentCreateCommand);

    /**
     * 해당 결제번호에 해당되는 결제를 승인 요청한다.
     *
     * @param paymentNo 결제 번호
     */
    void approvePayment(String paymentNo);
}
