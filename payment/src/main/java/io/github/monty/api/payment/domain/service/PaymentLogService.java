package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import io.github.monty.api.payment.domain.model.query.PaymentLogListQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentLogListResultVO;
import io.github.monty.api.payment.domain.repository.PaymentLogCustomRepository;
import io.github.monty.api.payment.infrastructure.jpa.mapper.PaymentLogListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private final PaymentLogCustomRepository paymentLogCustomRepository;

    private final PaymentLogListMapper paymentLogListMapper;

    public PaymentLogListResultVO requestPaymentLogList(PaymentLogListQuery paymentLogListQuery) {
        Page<PaymentLog> paymentLogPage = paymentLogCustomRepository.findAll(paymentLogListQuery.paymentNo(), paymentLogListQuery.pageable());
        List<PaymentLog> paymentLogList = paymentLogPage.get().toList();
        return paymentLogListMapper.mapToVo(paymentLogList, (long) paymentLogPage.getTotalPages(), paymentLogPage.getTotalElements());
    }
}
