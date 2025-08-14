package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureVO;

public interface PaymentStrategy {

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
    PaymentSignatureVO getSignature(PaymentSignatureQuery paymentSignatureQuery);
}
