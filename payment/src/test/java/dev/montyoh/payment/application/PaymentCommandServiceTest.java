package dev.montyoh.payment.application;

import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.command.PaymentApprovalCommand;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.domain.model.command.PaymentNetworkCancelCommand;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCreateResVo;
import dev.montyoh.payment.domain.model.vo.PaymentCreateResVo;
import dev.montyoh.payment.domain.service.PaymentService;
import dev.montyoh.payment.domain.strategy.PaymentCancelStrategy;
import dev.montyoh.payment.domain.strategy.PaymentCancelStrategyFactory;
import dev.montyoh.payment.domain.strategy.PaymentStrategy;
import dev.montyoh.payment.domain.strategy.PaymentStrategyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceTest {

    @InjectMocks
    private PaymentCommandService paymentCommandService;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;
    @Mock
    private PaymentCancelStrategyFactory paymentCancelStrategyFactory;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentStrategy paymentStrategy;
    @Mock
    private PaymentCancelStrategy paymentCancelStrategy;

    @Test
    @DisplayName("결제 정보를 생성한다.")
    void createPayment_success() {
        //  given
        InicisPaymentCreateCommand command = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .build();
        PaymentCreateResVo expected = InicisPaymentCreateResVo.builder()
                .paymentNo("testPaymentNo")
                .build();
        given(paymentStrategyFactory.getPaymentStrategy(PgProviderType.INICIS)).willReturn(paymentStrategy);
        given(paymentStrategy.createPayment(any())).willReturn(expected);

        //  when
        PaymentCreateResVo actual = paymentCommandService.createPayment(command);

        //  then
        assertThat(actual.getPaymentNo()).isEqualTo(expected.getPaymentNo());
    }

    @Test
    @DisplayName("결제를 승인한다.")
    void approvePayment_success() {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentApprovalCommand command = PaymentApprovalCommand.builder().paymentNo(paymentNo).build();
        given(paymentStrategyFactory.getPaymentStrategy(paymentNo)).willReturn(paymentStrategy);
        willDoNothing().given(paymentService).markAsRequested(paymentNo);
        willDoNothing().given(paymentStrategy).approvePayment(paymentNo);

        //  when
        paymentCommandService.approvePayment(command);

        //  then
        assertAll(
                () -> verify(paymentService, times(1)).markAsRequested(paymentNo),
                () -> verify(paymentStrategy, times(1)).approvePayment(paymentNo)
        );
    }

    @Test
    @DisplayName("결제 승인 실패 시 망취소를 요청하고 예외를 다시 던진다.")
    void approvePayment_fail_triggers_network_cancel() {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentApprovalCommand command = PaymentApprovalCommand.builder().paymentNo(paymentNo).build();
        given(paymentStrategyFactory.getPaymentStrategy(paymentNo)).willReturn(paymentStrategy);
        given(paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo)).willReturn(paymentCancelStrategy);
        willThrow(new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL)).given(paymentStrategy).approvePayment(paymentNo);

        //  when, then
        assertAll(
                () -> assertThatThrownBy(() -> paymentCommandService.approvePayment(command))
                        .isInstanceOf(ApplicationException.class),
                () -> verify(paymentCancelStrategy, times(1)).networkCancelPayment(paymentNo)
        );
    }

    @Test
    @DisplayName("결제를 취소한다.")
    void cancelPayment_success() {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentCancelCommand command = PaymentCancelCommand.builder()
                .paymentNo(paymentNo)
                .cancelReason("테스트 취소")
                .build();
        willDoNothing().given(paymentService).isEnableCancel(paymentNo);
        given(paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo)).willReturn(paymentCancelStrategy);
        willDoNothing().given(paymentCancelStrategy).cancelPayment(any());

        //  when
        paymentCommandService.cancelPayment(command);

        //  then
        assertAll(
                () -> verify(paymentService, times(1)).isEnableCancel(paymentNo),
                () -> verify(paymentCancelStrategy, times(1)).cancelPayment(command)
        );
    }

    @Test
    @DisplayName("결제 망취소를 요청한다.")
    void networkCancelPayment_success() {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentNetworkCancelCommand command = PaymentNetworkCancelCommand.builder().paymentNo(paymentNo).build();
        willDoNothing().given(paymentService).isEnableCancel(paymentNo);
        given(paymentCancelStrategyFactory.getPaymentCancelStrategy(paymentNo)).willReturn(paymentCancelStrategy);
        willDoNothing().given(paymentCancelStrategy).networkCancelPayment(anyString());

        //  when
        paymentCommandService.networkCancelPayment(command);

        //  then
        assertAll(
                () -> verify(paymentService, times(1)).isEnableCancel(paymentNo),
                () -> verify(paymentCancelStrategy, times(1)).networkCancelPayment(paymentNo)
        );
    }
}
