package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.EncryptType;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.common.utils.EncryptUtils;
import dev.montyoh.payment.domain.model.aggregate.NicepayPayment;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentCancelResVo;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentNetworkCancelResVo;
import dev.montyoh.payment.domain.repository.NicepayRepository;
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
class NicepayPaymentCancelStrategyTest {

    @InjectMocks
    NicepayPaymentCancelStrategy nicepayPaymentCancelStrategy;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    NicepayRepository nicepayRepository;

    private static final String TEST_MERCHANT_KEY = "testMerchantKey";
    private static final String TEST_MID = "testMid";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nicepayPaymentCancelStrategy, "merchantKey", TEST_MERCHANT_KEY);
        ReflectionTestUtils.setField(nicepayPaymentCancelStrategy, "nicepayMid", TEST_MID);
    }

    private NicepayPayment buildNicepayPayment() {
        String authToken = "testAuthToken";
        String price = "10000";
        String plainText = authToken + TEST_MID + price + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);
        NicepayPaymentCreateCommand cmd = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo").price(10000L)
                .authToken(authToken).signature(validSignature)
                .nextApprovalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        return new NicepayPayment("testPaymentNo", cmd);
    }

    @Test
    @DisplayName("나이스페이 결제 취소 타입을 반환한다.")
    void getPaymentType() {
        assertThat(nicepayPaymentCancelStrategy.getPaymentType()).isEqualTo(PgProviderType.NICEPAY);
    }

    @Test
    @DisplayName("나이스페이 결제를 망취소 처리한다.")
    void networkCancelPayment_success() {
        //  given
        NicepayPayment payment = buildNicepayPayment();
        String tid = "testTid";
        String cancelAmount = "10000";
        String plainText = tid + TEST_MID + cancelAmount + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);
        NicepayPaymentNetworkCancelResVo resVo = NicepayPaymentNetworkCancelResVo.builder()
                .resultMessage("망취소 완료").tid(tid).cancelAmount(cancelAmount).signature(validSignature).build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestNetworkCancelPayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        nicepayPaymentCancelStrategy.networkCancelPayment("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 망취소 시 예외가 발생한다.")
    void networkCancelPayment_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentCancelStrategy.networkCancelPayment("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("망취소 외부 API 실패 시 결제 상태가 NETWORK_CANCELED_FAIL 로 변경된다.")
    void networkCancelPayment_fail_generalException() {
        //  given
        NicepayPayment payment = buildNicepayPayment();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestNetworkCancelPayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentCancelStrategy.networkCancelPayment("testPaymentNo"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("나이스페이 결제를 취소 처리한다.")
    void cancelPayment_success() {
        //  given
        NicepayPayment payment = buildNicepayPayment();
        String cancelTransactionId = "testCancelTid";
        String cancelAmount = "10000";
        String plainText = cancelTransactionId + TEST_MID + cancelAmount + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);
        NicepayPaymentCancelResVo resVo = NicepayPaymentCancelResVo.builder()
                .resultMessage("취소 완료").reason("테스트 취소")
                .cancelTransactionId(cancelTransactionId).cancelAmount(cancelAmount).signature(validSignature).build();
        PaymentCancelCommand command = PaymentCancelCommand.builder()
                .paymentNo("testPaymentNo").cancelReason("테스트 취소").build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestCancelPayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        nicepayPaymentCancelStrategy.cancelPayment(command);
    }

    @Test
    @DisplayName("취소 외부 API 실패 시 결제 상태가 CANCELED_FAIL 로 변경된다.")
    void cancelPayment_fail_generalException() {
        //  given
        NicepayPayment payment = buildNicepayPayment();
        PaymentCancelCommand command = PaymentCancelCommand.builder()
                .paymentNo("testPaymentNo").cancelReason("테스트 취소").build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestCancelPayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentCancelStrategy.cancelPayment(command))
                .isInstanceOf(RuntimeException.class);
    }
}
