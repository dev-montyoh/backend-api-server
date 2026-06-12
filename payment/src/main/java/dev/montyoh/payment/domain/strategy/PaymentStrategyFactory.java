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
public class PaymentStrategyFactory {
    private final PaymentRepository paymentRepository;
    private final Map<PgProviderType, PaymentStrategy> paymentServiceMap = new EnumMap<>(PgProviderType.class);

    @Autowired
    public PaymentStrategyFactory(List<PaymentStrategy> paymentStrategyList, PaymentRepository paymentRepository) {
        for (PaymentStrategy paymentStrategy : paymentStrategyList) {
            paymentServiceMap.put(paymentStrategy.getPaymentType(), paymentStrategy);
        }
        this.paymentRepository = paymentRepository;
    }

    /**
     * 특정 결제 서비스 제공자 타입에 맞는 결제 전략을 반환한다.
     *
     * @param PgProviderType 결제 서비스 제공자 타입
     * @return 결제 전략
     */
    public PaymentStrategy getPaymentStrategy(PgProviderType PgProviderType) {
        if (!paymentServiceMap.containsKey(PgProviderType)) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_SERVICE);
        }
        return paymentServiceMap.get(PgProviderType);
    }

    /**
     * 특정 결제 번호에 맞는 결제 전략을 반환한다.
     *
     * @param paymentNo 결제 번호
     * @return 결제 전략
     */
    public PaymentStrategy getPaymentStrategy(String paymentNo) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(paymentNo);
        Payment payment = paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
        return this.getPaymentStrategy(payment.getPgProviderType());
    }
}
