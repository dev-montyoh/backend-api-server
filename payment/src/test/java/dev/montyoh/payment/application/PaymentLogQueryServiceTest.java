package dev.montyoh.payment.application;

import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.domain.service.PaymentLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentLogQueryServiceTest {

    @InjectMocks
    private PaymentLogQueryService paymentLogQueryService;

    @Mock
    private PaymentLogService paymentLogService;

    @Test
    @DisplayName("결제 로그 목록을 조회한다.")
    void requestPaymentLogList_success() {
        //  given
        PaymentLogListQuery query = PaymentLogListQuery.builder()
                .paymentNo("testPaymentNo")
                .pageable(PageRequest.of(0, 50))
                .build();
        PaymentLogListResVo expected = PaymentLogListResVo.builder()
                .paymentLogList(List.of())
                .totalPages(0L)
                .totalCount(0L)
                .build();
        given(paymentLogService.requestPaymentLogList(any())).willReturn(expected);

        //  when
        PaymentLogListResVo actual = paymentLogQueryService.requestPaymentLogList(query);

        //  then
        assertAll(
                () -> assertThat(actual.paymentLogList()).isEqualTo(expected.paymentLogList()),
                () -> assertThat(actual.totalPages()).isEqualTo(expected.totalPages()),
                () -> assertThat(actual.totalCount()).isEqualTo(expected.totalCount())
        );
    }
}
