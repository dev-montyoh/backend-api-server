package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
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
    private final Map<PaymentServiceProviderType, PaymentCancelStrategy> paymentCancelStrategyMap = new EnumMap<>(PaymentServiceProviderType.class);

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
     * @param PaymentServiceProviderType 결제 서비스 제공자 타입
     * @return 결제 전략
     */
    public PaymentCancelStrategy getPaymentCancelStrategy(PaymentServiceProviderType PaymentServiceProviderType) {
        if (!paymentCancelStrategyMap.containsKey(PaymentServiceProviderType)) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_SERVICE);
        }
        return paymentCancelStrategyMap.get(PaymentServiceProviderType);
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
        return this.getPaymentCancelStrategy(payment.getPaymentServiceProviderType());
    }
}
