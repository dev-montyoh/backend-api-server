package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCancelResVo;
import dev.montyoh.payment.domain.model.vo.InicisPaymentNetworkCancelResVo;
import dev.montyoh.payment.domain.repository.InicisRepository;
import dev.montyoh.payment.domain.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InicisPaymentCancelStrategyTest {

    @InjectMocks
    InicisPaymentCancelStrategy inicisPaymentCancelStrategy;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    InicisRepository inicisRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(inicisPaymentCancelStrategy, "inicisSignKey", "testSignKey");
        ReflectionTestUtils.setField(inicisPaymentCancelStrategy, "inicisMid", "testMid");
        ReflectionTestUtils.setField(inicisPaymentCancelStrategy, "iniApiKey", "testApiKey");
    }

    private InicisPayment buildInicisPayment() {
        InicisPaymentCreateCommand cmd = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo").price(10000L)
                .authToken("testAuthToken").idcCode("testIdcCode")
                .approvalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        return new InicisPayment("testPaymentNo", cmd);
    }

    @Test
    @DisplayName("이니시스 결제 취소 타입을 반환한다.")
    void getPaymentType() {
        assertThat(inicisPaymentCancelStrategy.getPaymentType()).isEqualTo(PgProviderType.INICIS);
    }

    @Test
    @DisplayName("이니시스 결제를 망취소 처리한다.")
    void networkCancelPayment_success() {
        //  given
        InicisPayment payment = buildInicisPayment();
        InicisPaymentNetworkCancelResVo resVo = InicisPaymentNetworkCancelResVo.builder()
                .resultMessage("망취소 완료").tid("testTid").build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestNetworkCancelPayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        inicisPaymentCancelStrategy.networkCancelPayment("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 망취소 시 예외가 발생한다.")
    void networkCancelPayment_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> inicisPaymentCancelStrategy.networkCancelPayment("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("망취소 외부 API 실패 시 결제 상태가 NETWORK_CANCELED_FAIL 로 변경된다.")
    void networkCancelPayment_fail_generalException() {
        //  given
        InicisPayment payment = buildInicisPayment();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestNetworkCancelPayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> inicisPaymentCancelStrategy.networkCancelPayment("testPaymentNo"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("이니시스 결제를 취소 처리한다.")
    void cancelPayment_success() {
        //  given
        InicisPayment payment = buildInicisPayment();
        PaymentCancelCommand command = PaymentCancelCommand.builder()
                .paymentNo("testPaymentNo").cancelReason("테스트 취소").build();
        InicisPaymentCancelResVo resVo = InicisPaymentCancelResVo.builder()
                .resultMessage("취소 완료").reason("테스트 취소").build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestCancelPayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        inicisPaymentCancelStrategy.cancelPayment(command);
    }

    @Test
    @DisplayName("취소 외부 API 실패 시 결제 상태가 CANCELED_FAIL 로 변경된다.")
    void cancelPayment_fail_generalException() {
        //  given
        InicisPayment payment = buildInicisPayment();
        PaymentCancelCommand command = PaymentCancelCommand.builder()
                .paymentNo("testPaymentNo").cancelReason("테스트 취소").build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestCancelPayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> inicisPaymentCancelStrategy.cancelPayment(command))
                .isInstanceOf(RuntimeException.class);
    }
}
