package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;

public interface PaymentCancelService {
    /**
     * 해당 전략의 결제 타입을 반환한다.
     *
     * @return 결제 타입
     */
    PaymentServiceProviderType getPaymentType();

    /**
     * 해당 결제번호에 해당되는 결제를 망취소 요청한다.
     *
     * @param paymentNo 결제 번호
     */
    void networkCancelPayment(String paymentNo);

    /**
     * 해당 결제번호에 해당되는 결제를 취소 요청한다.
     *
     * @param paymentCancelCommand 결제 취소 요청 Command
     */
    void cancelPayment(PaymentCancelCommand paymentCancelCommand);

    void partialCancelPayment();

    void virtualAccountCancelPayment();
}
