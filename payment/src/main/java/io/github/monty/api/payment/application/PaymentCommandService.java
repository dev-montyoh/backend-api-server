package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.command.InicisPaymentApproveCommand;
import io.github.monty.api.payment.domain.model.command.PaymentApproveCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import io.github.monty.api.payment.domain.service.PaymentServiceFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCommandService {

    private final PaymentServiceFactory paymentServiceFactory;

    /**
     * 결제 정보 생성
     *
     * @param paymentCreateCommand 결제 정보 생성 요청 Command
     */
    @Transactional
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentCreateCommand.getPaymentType());
        return paymentService.createPayment(paymentCreateCommand);
    }

    /**
     * 결제 승인
     *
     * @param paymentApproveCommand 이니시스 결제 승인 요청 Command
     */
    @Transactional
    public void approvePayment(PaymentApproveCommand paymentApproveCommand) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentApproveCommand.getPaymentType());
        paymentService.approvePayment(paymentApproveCommand);
    }
}
