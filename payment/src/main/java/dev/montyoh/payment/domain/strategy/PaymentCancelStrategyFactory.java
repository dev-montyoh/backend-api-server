package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.aggregate.Payment;
import dev.montyoh.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentCancelStrategyFactory {
    private final PaymentRepository paymentRepository;
    private final Map<PgProviderType, PaymentCancelStrategy> paymentCancelStrategyMap = new EnumMap<>(PgProviderType.class);

    @Autowired
    public PaymentCancelStrategyFactory(List<PaymentCancelStrategy> paymentCancelStrategyList, PaymentRepository paymentRepository) {
        for (PaymentCancelStrategy paymentCancelStrategy : paymentCancelStrategyList) {
            paymentCancelStrategyMap.put(paymentCancelStrategy.getPaymentType(), paymentCancelStrategy);
        }
        this.paymentRepository = paymentRepository;
    }

    /**
     * 특정 결제 서비스 제공자 타입에 맞는 결제 전략을 반환한다.
     *
     * @param PgProviderType 결제 서비스 제공자 타입
     * @return 결제 전략
     */
    public PaymentCancelStrategy getPaymentCancelStrategy(PgProviderType PgProviderType) {
        if (!paymentCancelStrategyMap.containsKey(PgProviderType)) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_SERVICE);
        }
        return paymentCancelStrategyMap.get(PgProviderType);
    }

    /**
     * 특정 결제 번호에 맞는 결제 전략을 반환한다.
     *
     * @param paymentNo 결제 번호
     * @return 결제 전략
     */
    public PaymentCancelStrategy getPaymentCancelStrategy(String paymentNo) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(paymentNo);
        Payment payment = paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
        return this.getPaymentCancelStrategy(payment.getPgProviderType());
    }
}
