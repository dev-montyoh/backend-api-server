package dev.montyoh.payment.application;

import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.domain.service.PaymentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentLogQueryService {
    private final PaymentLogService paymentLogService;

    /**
     * 결제 로그 목록 조회
     *
     * @param paymentLogListQuery 결제 로그 목록 조회 쿼리
     * @return 결제 로그 목록 조회 결과
     */
    public PaymentLogListResVo requestPaymentLogList(PaymentLogListQuery paymentLogListQuery) {
        return paymentLogService.requestPaymentLogList(paymentLogListQuery);
    }
}
