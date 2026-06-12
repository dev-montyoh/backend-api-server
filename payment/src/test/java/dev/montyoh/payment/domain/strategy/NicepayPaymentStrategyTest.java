package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.EncryptType;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.common.utils.EncryptUtils;
import dev.montyoh.payment.domain.model.aggregate.NicepayPayment;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.query.NicepayPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentApprovalResVo;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import dev.montyoh.payment.domain.model.vo.PaymentCreateResVo;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NicepayPaymentStrategyTest {

    @InjectMocks
    NicepayPaymentStrategy nicepayPaymentStrategy;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    NicepayRepository nicepayRepository;

    private static final String TEST_MERCHANT_KEY = "testMerchantKey";
    private static final String TEST_MID = "testMid";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(nicepayPaymentStrategy, "merchantKey", TEST_MERCHANT_KEY);
        ReflectionTestUtils.setField(nicepayPaymentStrategy, "nicepayMid", TEST_MID);
    }

    @Test
    @DisplayName("나이스페이 결제 타입을 반환한다.")
    void getPaymentType() {
        assertThat(nicepayPaymentStrategy.getPaymentType()).isEqualTo(PgProviderType.NICEPAY);
    }

    @Test
    @DisplayName("나이스페이 결제 인증 정보를 생성한다.")
    void getSignature_success() {
        //  given
        NicepayPaymentSignatureQuery query = NicepayPaymentSignatureQuery.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .price("10000")
                .build();

        //  when
        NicepayPaymentSignatureResVo actual = (NicepayPaymentSignatureResVo) nicepayPaymentStrategy.getSignature(query);

        //  then
        assertThat(actual.getMid()).isEqualTo(TEST_MID);
        assertThat(actual.getEdiDate()).isNotBlank();
        assertThat(actual.getSignData()).isNotBlank();
    }

    @Test
    @DisplayName("나이스페이 결제 데이터를 저장한다.")
    void createPayment_success() {
        //  given
        String authToken = "testAuthToken";
        String price = "10000";
        String plainText = authToken + TEST_MID + price + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);

        NicepayPaymentCreateCommand command = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo")
                .price(10000L)
                .authToken(authToken)
                .signature(validSignature)
                .nextApprovalUrl("http://approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        NicepayPayment payment = new NicepayPayment("testPaymentNo", command);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when
        PaymentCreateResVo actual = nicepayPaymentStrategy.createPayment(command);

        //  then
        assertThat(actual.getPaymentNo()).isEqualTo("testPaymentNo");
    }

    @Test
    @DisplayName("유효하지 않은 signature로 결제 데이터 저장 시 예외가 발생한다.")
    void createPayment_invalidSignature() {
        //  given
        NicepayPaymentCreateCommand command = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo").price(10000L)
                .authToken("testAuthToken").signature("invalidSignature")
                .nextApprovalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentStrategy.createPayment(command))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("나이스페이 결제를 승인 처리한다.")
    void approvePayment_success() {
        //  given
        String authToken = "testAuthToken";
        String price = "10000";
        String plainText = authToken + TEST_MID + price + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);

        NicepayPaymentCreateCommand command = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo").price(10000L)
                .authToken(authToken).signature(validSignature)
                .nextApprovalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        NicepayPayment payment = new NicepayPayment("testPaymentNo", command);

        String tid = "testTid";
        String cancelAmount = "10000";
        String approvalSignaturePlain = tid + TEST_MID + cancelAmount + TEST_MERCHANT_KEY;
        String approvalSignature = EncryptUtils.encrypt(approvalSignaturePlain, EncryptType.SHA256);
        NicepayPaymentApprovalResVo resVo = NicepayPaymentApprovalResVo.builder()
                .resultMessage("승인 완료").tid(tid).amount(cancelAmount)
                .signature(approvalSignature).approvalDateTime(LocalDateTime.now()).build();

        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestApprovePayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        nicepayPaymentStrategy.approvePayment("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 승인 시 예외가 발생한다.")
    void approvePayment_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentStrategy.approvePayment("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("나이스페이 결제 승인 실패 시 결제 상태가 DECLINED 로 변경된다.")
    void approvePayment_fail_generalException() {
        //  given
        String authToken = "testAuthToken";
        String price = "10000";
        String plainText = authToken + TEST_MID + price + TEST_MERCHANT_KEY;
        String validSignature = EncryptUtils.encrypt(plainText, EncryptType.SHA256);

        NicepayPaymentCreateCommand command = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY).orderNo("testOrderNo").price(10000L)
                .authToken(authToken).signature(validSignature)
                .nextApprovalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        NicepayPayment payment = new NicepayPayment("testPaymentNo", command);
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(nicepayRepository.requestApprovePayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> nicepayPaymentStrategy.approvePayment("testPaymentNo"))
                .isInstanceOf(RuntimeException.class);
    }
}
