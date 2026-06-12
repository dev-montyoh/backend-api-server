package dev.montyoh.payment.application;

import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.query.PaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.model.vo.PaymentSignatureResVo;
import dev.montyoh.payment.domain.service.PaymentService;
import dev.montyoh.payment.domain.strategy.PaymentStrategy;
import dev.montyoh.payment.domain.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentService paymentService;

    /**
     * 결제 Signature 생성
     *
     * @param paymentSignatureQuery 결제 Signature 생성 요청 쿼리
     * @return 생성 결과
     */
    public PaymentSignatureResVo requestPaymentSignature(PaymentSignatureQuery paymentSignatureQuery) {
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentSignatureQuery.getPgProviderType());
        return paymentStrategy.getSignature(paymentSignatureQuery);
    }

    /**
     * 결제 목록 조회
     *
     * @param paymentListQuery 결제 목록 조회 쿼리
     * @return 결제 목록 조회 결과
     */
    public PaymentListResVo requestPaymentList(PaymentListQuery paymentListQuery) {
        return paymentService.requestPaymentList(paymentListQuery);
    }
}
