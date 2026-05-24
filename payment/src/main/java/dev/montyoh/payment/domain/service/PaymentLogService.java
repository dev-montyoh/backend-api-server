package dev.montyoh.payment.domain.service;

import dev.montyoh.payment.domain.model.entity.PaymentLog;
import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.domain.repository.PaymentLogCustomRepository;
import dev.montyoh.payment.infrastructure.jpa.mapper.PaymentLogListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private final PaymentLogCustomRepository paymentLogCustomRepository;

    private final PaymentLogListMapper paymentLogListMapper;

    public PaymentLogListResVo requestPaymentLogList(PaymentLogListQuery paymentLogListQuery) {
        Page<PaymentLog> paymentLogPage = paymentLogCustomRepository.findAll(paymentLogListQuery.paymentNo(), paymentLogListQuery.pageable());
        List<PaymentLog> paymentLogList = paymentLogPage.get().toList();
        return paymentLogListMapper.mapToVo(paymentLogList, (long) paymentLogPage.getTotalPages(), paymentLogPage.getTotalElements());
    }
}
