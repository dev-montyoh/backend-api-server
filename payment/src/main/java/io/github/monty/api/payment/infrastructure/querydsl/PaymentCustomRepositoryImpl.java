package io.github.monty.api.payment.infrastructure.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.aggregate.QPayment;
import io.github.monty.api.payment.domain.model.aggregate.QPaymentCancel;
import io.github.monty.api.payment.domain.repository.PaymentCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 결제 취소 데이터까지 같이 조회
     *
     * @param paymentNo 결제 번호
     * @return 결제 엔티티
     */
    @Override
    public Optional<Payment> findByPaymentNoWithCancelList(String paymentNo) {
        QPayment qPayment = QPayment.payment;
        QPaymentCancel qPaymentCancel = QPaymentCancel.paymentCancel;

        Payment payment = jpaQueryFactory.selectFrom(qPayment)
                .leftJoin(qPayment.paymentCancelList, qPaymentCancel)
                .fetchJoin()
                .where(qPayment.paymentNo.eq(paymentNo))
                .distinct()
                .fetchOne();
        return Optional.ofNullable(payment);
    }
}
