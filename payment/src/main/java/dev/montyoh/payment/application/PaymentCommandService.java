package dev.montyoh.payment.application;

import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.command.PaymentApprovalCommand;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.domain.model.command.PaymentCreateCommand;
import dev.montyoh.payment.domain.model.command.PaymentNetworkCancelCommand;
import dev.montyoh.payment.domain.model.vo.PaymentCreateResVo;
import dev.montyoh.payment.domain.service.PaymentService;
import dev.montyoh.payment.domain.strategy.PaymentCancelStrategy;
import dev.montyoh.payment.domain.strategy.PaymentCancelStrategyFactory;
import dev.montyoh.payment.domain.strategy.PaymentStrategy;
import dev.montyoh.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCommandService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentCancelStrategyFactory paymentCancelStrategyFactory;

    private final PaymentService paymentService;

    /**
     * 결제 정보 생성
     *
     * @param paymentCreateCommand 결제 정보 생성 요청 Command
     */
    @Transactional
    public PaymentCreateResVo createPayment(PaymentCreateCommand paymentCreateCommand) {
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
        paymentService.isEnableCancel(paymentNo);
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
        paymentService.isEnableCancel(paymentNo);
        PaymentCancelStrategy paymentCancelStrategy = paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo);
        paymentCancelStrategy.networkCancelPayment(paymentNo);
    }
}
