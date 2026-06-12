package dev.montyoh.payment.application;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.query.InicisPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.vo.InicisPaymentSignatureResVo;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.model.vo.PaymentSignatureResVo;
import dev.montyoh.payment.domain.service.PaymentService;
import dev.montyoh.payment.domain.strategy.PaymentStrategy;
import dev.montyoh.payment.domain.strategy.PaymentStrategyFactory;
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
class PaymentQueryServiceTest {

    @InjectMocks
    private PaymentQueryService paymentQueryService;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentStrategy paymentStrategy;

    @Test
    @DisplayName("결제 Signature 를 생성한다.")
    void requestPaymentSignature_success() {
        //  given
        InicisPaymentSignatureQuery query = InicisPaymentSignatureQuery.builder()
                .pgProviderType(PgProviderType.INICIS)
                .oid("testOid")
                .price("10000")
                .build();
        InicisPaymentSignatureResVo expected = InicisPaymentSignatureResVo.builder()
                .signature("testSignature")
                .verification("testVerification")
                .mKey("testMKey")
                .mid("testMid")
                .timestamp(12345678L)
                .build();
        given(paymentStrategyFactory.getPaymentStrategy(PgProviderType.INICIS)).willReturn(paymentStrategy);
        given(paymentStrategy.getSignature(any())).willReturn(expected);

        //  when
        PaymentSignatureResVo actual = paymentQueryService.requestPaymentSignature(query);

        //  then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("결제 목록을 조회한다.")
    void requestPaymentList_success() {
        //  given
        PaymentListQuery query = PaymentListQuery.builder()
                .pageable(PageRequest.of(0, 50))
                .build();
        PaymentListResVo expected = PaymentListResVo.builder()
                .paymentList(List.of())
                .totalPages(0L)
                .totalCount(0L)
                .build();
        given(paymentService.requestPaymentList(any())).willReturn(expected);

        //  when
        PaymentListResVo actual = paymentQueryService.requestPaymentList(query);

        //  then
        assertAll(
                () -> assertThat(actual.paymentList()).isEqualTo(expected.paymentList()),
                () -> assertThat(actual.totalPages()).isEqualTo(expected.totalPages()),
                () -> assertThat(actual.totalCount()).isEqualTo(expected.totalCount())
        );
    }
}
