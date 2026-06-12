package dev.montyoh.payment.domain.service;

import dev.montyoh.payment.domain.model.entity.PaymentLog;
import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.domain.repository.PaymentLogCustomRepository;
import dev.montyoh.payment.infrastructure.jpa.mapper.PaymentLogListMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentLogServiceTest {

    @InjectMocks
    PaymentLogService paymentLogService;

    @Mock
    PaymentLogCustomRepository paymentLogCustomRepository;
    @Mock
    PaymentLogListMapper paymentLogListMapper;

    @Test
    @DisplayName("결제 로그 목록을 조회한다.")
    void requestPaymentLogList_success() {
        //  given
        PageRequest pageable = PageRequest.of(0, 50);
        PaymentLogListQuery query = PaymentLogListQuery.builder()
                .paymentNo("testPaymentNo")
                .pageable(pageable)
                .build();
        Page<PaymentLog> page = new PageImpl<>(List.of(), pageable, 0);
        PaymentLogListResVo resVo = PaymentLogListResVo.builder().paymentLogList(List.of()).totalPages(0L).totalCount(0L).build();
        given(paymentLogCustomRepository.findAll("testPaymentNo", pageable)).willReturn(page);
        given(paymentLogListMapper.mapToVo(any(), any(), any())).willReturn(resVo);

        //  when
        PaymentLogListResVo actual = paymentLogService.requestPaymentLogList(query);

        //  then
        assertThat(actual).isEqualTo(resVo);
    }
}
