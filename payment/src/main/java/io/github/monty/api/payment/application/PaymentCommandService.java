package io.github.monty.api.payment.application;

import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.domain.model.command.PaymentApprovalCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.command.PaymentNetworkCancelCommand;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.service.PaymentCancelService;
import io.github.monty.api.payment.domain.service.PaymentCancelServiceFactory;
import io.github.monty.api.payment.domain.service.PaymentService;
import io.github.monty.api.payment.domain.service.PaymentServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCommandService {

    private final PaymentServiceFactory paymentServiceFactory;
    private final PaymentCancelServiceFactory paymentCancelServiceFactory;

    /**
     * 결제 정보 생성
     *
     * @param paymentCreateCommand 결제 정보 생성 요청 Command
     */
    @Transactional
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentCreateCommand.getPaymentServiceProviderType());
        return paymentService.createPayment(paymentCreateCommand);
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
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentNo);
        try {
            paymentService.approvePayment(paymentNo);
        } catch (Exception e) {
            PaymentCancelService paymentCancelService = paymentCancelServiceFactory.getPaymentCancelService(paymentNo);
            paymentCancelService.networkCancelPayment(paymentNo);
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
        PaymentCancelService paymentCancelService = paymentCancelServiceFactory.getPaymentCancelService(paymentNo);
        paymentCancelService.cancelPayment(paymentCancelCommand);
    }

    /**
     * 결제 망 취소
     *
     * @param paymentNetworkCancelCommand 결제 망 취소 요청 Command
     */
    public void networkCancelPayment(PaymentNetworkCancelCommand paymentNetworkCancelCommand) {
        String paymentNo = paymentNetworkCancelCommand.getPaymentNo();
        PaymentCancelService paymentCancelService = paymentCancelServiceFactory.getPaymentCancelService(paymentNo);
        paymentCancelService.networkCancelPayment(paymentNo);
    }
}
