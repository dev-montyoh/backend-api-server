package io.github.monty.api.payment.infrastructure.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.monty.api.payment.domain.model.aggregate.QPayment;
import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import io.github.monty.api.payment.domain.model.entity.QPaymentLog;
import io.github.monty.api.payment.domain.repository.PaymentLogCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentLogCustomRepositoryImpl implements PaymentLogCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 해당 결제 번호의 결제 로그 목록을 조회한다.
     *
     * @param paymentNo 결제 번호
     * @param pageable  페이징 정보
     * @return 조회 결과
     */
    @Override
    public Page<PaymentLog> findAll(String paymentNo, Pageable pageable) {
        QPayment qPayment = QPayment.payment;
        QPaymentLog qPaymentLog = QPaymentLog.paymentLog;

        List<PaymentLog> paymentLogList = jpaQueryFactory.selectFrom(qPaymentLog)
                .leftJoin(qPaymentLog.payment, qPayment)
                .where(qPayment.paymentNo.eq(paymentNo))
                .orderBy(qPaymentLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory.select(qPaymentLog.count())
                .from(qPaymentLog)
                .leftJoin(qPaymentLog.payment, qPayment)
                .where(qPayment.paymentNo.eq(paymentNo))
                .fetchOne();
        if (ObjectUtils.isEmpty(totalCount)) totalCount = 0L;

        return new PageImpl<>(paymentLogList, pageable, totalCount);
    }
}
