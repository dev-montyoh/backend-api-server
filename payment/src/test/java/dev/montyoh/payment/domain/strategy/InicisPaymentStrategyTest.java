package dev.montyoh.payment.domain.strategy;

import dev.montyoh.payment.common.constants.EncryptType;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.common.utils.EncryptUtils;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.query.InicisPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.InicisPaymentApprovalResVo;
import dev.montyoh.payment.domain.model.vo.InicisPaymentSignatureResVo;
import dev.montyoh.payment.domain.model.vo.PaymentCreateResVo;
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

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InicisPaymentStrategyTest {

    @InjectMocks
    InicisPaymentStrategy inicisPaymentStrategy;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    InicisRepository inicisRepository;

    private static final String TEST_SIGN_KEY = "testSignKey";
    private static final String TEST_MID = "testMid";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(inicisPaymentStrategy, "inicisSignKey", TEST_SIGN_KEY);
        ReflectionTestUtils.setField(inicisPaymentStrategy, "inicisMid", TEST_MID);
    }

    @Test
    @DisplayName("이니시스 결제 타입을 반환한다.")
    void getPaymentType() {
        assertThat(inicisPaymentStrategy.getPaymentType()).isEqualTo(PgProviderType.INICIS);
    }

    @Test
    @DisplayName("이니시스 결제 인증 정보를 생성한다.")
    void getSignature_success() {
        //  given
        InicisPaymentSignatureQuery query = InicisPaymentSignatureQuery.builder()
                .pgProviderType(PgProviderType.INICIS)
                .oid("testOid")
                .price("10000")
                .build();

        //  when
        InicisPaymentSignatureResVo actual = (InicisPaymentSignatureResVo) inicisPaymentStrategy.getSignature(query);

        //  then
        assertThat(actual.getMid()).isEqualTo(TEST_MID);
        assertThat(actual.getMKey()).isEqualTo(EncryptUtils.encrypt(TEST_SIGN_KEY, EncryptType.SHA256));
        assertThat(actual.getSignature()).isNotBlank();
        assertThat(actual.getVerification()).isNotBlank();
    }

    @Test
    @DisplayName("이니시스 결제 데이터를 저장한다.")
    void createPayment_success() {
        //  given
        InicisPaymentCreateCommand command = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .authToken("testAuthToken")
                .idcCode("testIdcCode")
                .approvalUrl("http://approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment("testPaymentNo", command);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when
        PaymentCreateResVo actual = inicisPaymentStrategy.createPayment(command);

        //  then
        assertThat(actual.getPaymentNo()).isEqualTo("testPaymentNo");
    }

    @Test
    @DisplayName("이니시스 결제를 승인 처리한다.")
    void approvePayment_success() {
        //  given
        InicisPaymentCreateCommand command = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo").price(10000L)
                .authToken("testAuthToken").idcCode("testIdcCode")
                .approvalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment("testPaymentNo", command);
        InicisPaymentApprovalResVo resVo = InicisPaymentApprovalResVo.builder()
                .resultMessage("승인 완료").tid("testTid").approvalDateTime(LocalDateTime.now()).build();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestApprovePayment(any())).willReturn(resVo);
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        inicisPaymentStrategy.approvePayment("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 승인 시 예외가 발생한다.")
    void approvePayment_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> inicisPaymentStrategy.approvePayment("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("이니시스 결제 승인 실패 시 결제 상태가 DECLINED 로 변경된다.")
    void approvePayment_fail_generalException() {
        //  given
        InicisPaymentCreateCommand command = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo").price(10000L)
                .authToken("testAuthToken").idcCode("testIdcCode")
                .approvalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment("testPaymentNo", command);
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(inicisRepository.requestApprovePayment(any())).willThrow(new RuntimeException("외부 API 오류"));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then
        assertThatThrownBy(() -> inicisPaymentStrategy.approvePayment("testPaymentNo"))
                .isInstanceOf(RuntimeException.class);
    }
}
