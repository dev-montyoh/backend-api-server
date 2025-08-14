package io.github.monty.api.payment.application;

import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureVO;
import io.github.monty.api.payment.domain.service.PaymentService;
import io.github.monty.api.payment.domain.service.PaymentServiceFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentServiceFactory paymentServiceFactory;

    /**
     * 결제 Signature 생성
     *
     * @param paymentSignatureQuery 결제 Signature 생성 요청 쿼리
     * @return 생성 결과
     */
    public PaymentSignatureVO requestPaymentSignature(PaymentSignatureQuery paymentSignatureQuery) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentSignatureQuery.getPaymentType());
        return paymentService.getSignature(paymentSignatureQuery);
    }

    /**
     * 결제 정보 생성
     * @param paymentCreateCommand 결제 정보 생성 요청 커맨드
     */
    @Transactional
    public void createPayment(PaymentCreateCommand paymentCreateCommand) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentCreateCommand.getPaymentType());

    }
}
