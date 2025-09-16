package io.github.monty.api.payment.application;

import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.domain.model.command.PaymentApprovalCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.command.PaymentNetworkCancelCommand;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.strategy.PaymentCancelStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentCancelStrategyFactory;
import io.github.monty.api.payment.domain.strategy.PaymentStrategy;
import io.github.monty.api.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCommandService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentCancelStrategyFactory paymentCancelStrategyFactory;

    /**
     * 결제 정보 생성
     *
     * @param paymentCreateCommand 결제 정보 생성 요청 Command
     */
    @Transactional
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentCreateCommand.getPaymentServiceProviderType());
        return paymentStrategy.createPayment(paymentCreateCommand);
    }

    /**
     * 결제 승인
     * 오류 발생 시 망취소 요청
     *
     * @param paymentApprovalCommand 결제 승인 요청 Command
     */
    @Transactional(noRollbackFor = ApplicationException.class)
    public void approvePayment(PaymentApprovalCommand paymentApprovalCommand) {
        String paymentNo = paymentApprovalCommand.getPaymentNo();
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentNo);
        try {
            paymentStrategy.approvePayment(paymentNo);
        } catch (Exception e) {
            PaymentCancelStrategy paymentCancelStrategy = paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo);
            paymentCancelStrategy.networkCancelPayment(paymentNo);
            throw e;
        }
    }

    /**
     * 결제 취소
     *
     * @param paymentCancelCommand 결제 취소 요청 Command
     */
    @Transactional(noRollbackFor = ApplicationException.class)
    public void cancelPayment(PaymentCancelCommand paymentCancelCommand) {
        String paymentNo = paymentCancelCommand.getPaymentNo();
        PaymentCancelStrategy paymentCancelStrategy = paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo);
        paymentCancelStrategy.cancelPayment(paymentCancelCommand);
    }

    /**
     * 결제 망 취소
     *
     * @param paymentNetworkCancelCommand 결제 망 취소 요청 Command
     */
    public void networkCancelPayment(PaymentNetworkCancelCommand paymentNetworkCancelCommand) {
        String paymentNo = paymentNetworkCancelCommand.getPaymentNo();
        PaymentCancelStrategy paymentCancelStrategy = paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo);
        paymentCancelStrategy.networkCancelPayment(paymentNo);
    }
}
