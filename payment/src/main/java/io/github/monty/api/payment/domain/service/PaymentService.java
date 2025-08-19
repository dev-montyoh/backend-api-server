package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResultVO;

import java.math.BigInteger;
import java.util.UUID;

public interface PaymentService {
    String PAYMENT_NO_PREFIX = "PAY";

    /**
     * 해당 전략의 결제 타입을 반환한다.
     *
     * @return 결제 타입
     */
    PaymentType getPaymentType();

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    PaymentSignatureResultVO getSignature(PaymentSignatureQuery paymentSignatureQuery);

    /**
     * 해당 결제 정보를 바탕으로 결제 데이터를 저장한다.
     *
     * @param paymentCreateCommand 결제 인증 정보 저장 요청 Command
     * @return 저장 결과
     */
    PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand);

    /**
     * 해당 결제 수단의 결제번호 생성 후 반환
     * @param paymentType   결제 타입
     * @return  결제 번호
     */
    default String generatePaymentNo(PaymentType paymentType) {
        UUID uuid = UUID.randomUUID();
        String base62 = new BigInteger(uuid.toString().replace("-", ""), 16).toString(36);
        return PAYMENT_NO_PREFIX + paymentType.getCode() + base62;
    }
}
