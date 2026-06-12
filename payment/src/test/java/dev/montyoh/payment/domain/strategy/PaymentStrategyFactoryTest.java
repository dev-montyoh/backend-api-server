package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentStrategyFactoryTest {

    PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    PaymentStrategy inicisStrategy;
    @Mock
    PaymentStrategy nicepayStrategy;

    @BeforeEach
    void setUp() {
        given(inicisStrategy.getPaymentType()).willReturn(PgProviderType.INICIS);
        given(nicepayStrategy.getPaymentType()).willReturn(PgProviderType.NICEPAY);
        paymentStrategyFactory = new PaymentStrategyFactory(List.of(inicisStrategy, nicepayStrategy), paymentRepository);
    }

    @Test
    @DisplayName("PG사 타입으로 결제 전략을 조회한다.")
    void getPaymentStrategy_byType_success() {
        //  when
        PaymentStrategy actual = paymentStrategyFactory.getPaymentStrategy(PgProviderType.INICIS);

        //  then
        assertThat(actual).isEqualTo(inicisStrategy);
    }

    @Test
    @DisplayName("존재하지 않는 PG사 타입으로 조회 시 예외가 발생한다.")
    void getPaymentStrategy_byType_notFound() {
        //  given
        PaymentStrategyFactory emptyFactory = new PaymentStrategyFactory(List.of(), paymentRepository);

        //  when, then
        assertThatThrownBy(() -> emptyFactory.getPaymentStrategy(PgProviderType.INICIS))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("결제번호로 결제 전략을 조회한다.")
    void getPaymentStrategy_byPaymentNo_success() {
        //  given
        InicisPayment payment = new InicisPayment("testPaymentNo", InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS).orderNo("testOrderNo").price(10000L)
                .authToken("token").idcCode("idc").approvalUrl("http://a.url").networkCancelUrl("http://n.url")
                .build());
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));

        //  when
        PaymentStrategy actual = paymentStrategyFactory.getPaymentStrategy("testPaymentNo");

        //  then
        assertThat(actual).isEqualTo(inicisStrategy);
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 조회 시 예외가 발생한다.")
    void getPaymentStrategy_byPaymentNo_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> paymentStrategyFactory.getPaymentStrategy("invalid"))
                .isInstanceOf(ApplicationException.class);
    }
}
