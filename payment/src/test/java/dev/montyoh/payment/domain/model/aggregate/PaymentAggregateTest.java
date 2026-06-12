package dev.montyoh.payment.domain.model.aggregate;

import dev.montyoh.payment.common.constants.PaymentCancelType;
import dev.montyoh.payment.common.constants.PaymentStatus;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.InicisPaymentApprovalResVo;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCancelResVo;
import dev.montyoh.payment.domain.model.vo.InicisPaymentNetworkCancelResVo;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentApprovalResVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentAggregateTest {

    private InicisPayment buildInicisPayment() {
        InicisPaymentCreateCommand cmd = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .authToken("testAuthToken")
                .idcCode("testIdcCode")
                .approvalUrl("http://approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        return new InicisPayment("testPaymentNo", cmd);
    }

    @Test
    @DisplayName("이니시스 결제 객체를 생성하면 인증 상태로 초기화된다.")
    void inicisPayment_constructor() {
        InicisPayment payment = buildInicisPayment();

        assertAll(
                () -> assertThat(payment.getPaymentNo()).isEqualTo("testPaymentNo"),
                () -> assertThat(payment.getAmount()).isEqualTo(10000L),
                () -> assertThat(payment.getOrderNo()).isEqualTo("testOrderNo"),
                () -> assertThat(payment.getPgProviderType()).isEqualTo(PgProviderType.INICIS),
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.AUTHENTICATED),
                () -> assertThat(payment.getAuthToken()).isEqualTo("testAuthToken"),
                () -> assertThat(payment.getIdcCode()).isEqualTo("testIdcCode"),
                () -> assertThat(payment.getApprovalUrl()).isEqualTo("http://approval.url"),
                () -> assertThat(payment.getNetworkCancelUrl()).isEqualTo("http://network-cancel.url"),
                () -> assertThat(payment.getPaymentLogList()).hasSize(1),
                () -> assertThat(payment.getPaymentCancelList()).isEmpty(),
                () -> assertThat(payment.getTransactionId()).isNull(),
                () -> assertThat(payment.getApprovalDateTime()).isNull()
        );
    }

    @Test
    @DisplayName("나이스페이 결제 객체를 생성하면 인증 상태로 초기화된다.")
    void nicepayPayment_constructor() {
        NicepayPaymentCreateCommand cmd = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo")
                .price(20000L)
                .authToken("testAuthToken")
                .signature("testSignature")
                .nextApprovalUrl("http://next-approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        NicepayPayment payment = new NicepayPayment("testPaymentNo2", cmd);

        assertAll(
                () -> assertThat(payment.getPaymentNo()).isEqualTo("testPaymentNo2"),
                () -> assertThat(payment.getAmount()).isEqualTo(20000L),
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.AUTHENTICATED),
                () -> assertThat(payment.getAuthToken()).isEqualTo("testAuthToken"),
                () -> assertThat(payment.getSignature()).isEqualTo("testSignature"),
                () -> assertThat(payment.getNextApprovalUrl()).isEqualTo("http://next-approval.url"),
                () -> assertThat(payment.getNetworkCancelUrl()).isEqualTo("http://network-cancel.url"),
                () -> assertThat(payment.getPaymentLogList()).hasSize(1)
        );
    }

    @Test
    @DisplayName("결제 취소 객체를 생성하면 입력값이 설정된다.")
    void paymentCancel_constructor() {
        InicisPayment payment = buildInicisPayment();

        PaymentCancel cancel = new PaymentCancel(payment, 10000L, "테스트 취소 사유", PaymentCancelType.CANCEL);

        assertAll(
                () -> assertThat(cancel.getCancelAmount()).isEqualTo(10000L),
                () -> assertThat(cancel.getReason()).isEqualTo("테스트 취소 사유"),
                () -> assertThat(cancel.getPaymentCancelType()).isEqualTo(PaymentCancelType.CANCEL),
                () -> assertThat(cancel.getCancelTransactionId()).isNull()
        );
    }

    @Test
    @DisplayName("markAsRequested() 호출 시 결제 승인 요청 상태로 변경된다.")
    void markAsRequested() {
        InicisPayment payment = buildInicisPayment();

        payment.markAsRequested();

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REQUESTED),
                () -> assertThat(payment.getPaymentLogList()).hasSize(2)
        );
    }

    @Test
    @DisplayName("applyPaymentApprovalResult() 호출 시 승인 결과가 반영된다.")
    void applyPaymentApprovalResult() {
        InicisPayment payment = buildInicisPayment();
        LocalDateTime approvalTime = LocalDateTime.of(2026, 6, 12, 10, 0, 0);
        InicisPaymentApprovalResVo vo = InicisPaymentApprovalResVo.builder()
                .resultMessage("결제 승인 완료")
                .tid("testTid")
                .buyerPhoneNumber("010-1234-5678")
                .buyerEmail("test@test.com")
                .paymentMethod("CARD")
                .approvalDateTime(approvalTime)
                .build();

        payment.applyPaymentApprovalResult(vo);

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.APPROVED),
                () -> assertThat(payment.getTransactionId()).isEqualTo("testTid"),
                () -> assertThat(payment.getBuyerPhone()).isEqualTo("010-1234-5678"),
                () -> assertThat(payment.getBuyerEmail()).isEqualTo("test@test.com"),
                () -> assertThat(payment.getPaymentMethod()).isEqualTo("CARD"),
                () -> assertThat(payment.getApprovalDateTime()).isEqualTo(approvalTime),
                () -> assertThat(payment.getPaymentLogList()).hasSize(2)
        );
    }

    @Test
    @DisplayName("applyPaymentApprovalResult() - 나이스페이 vo로도 동작한다.")
    void applyPaymentApprovalResult_nicepay() {
        InicisPayment payment = buildInicisPayment();
        NicepayPaymentApprovalResVo vo = NicepayPaymentApprovalResVo.builder()
                .resultMessage("결제 승인 완료")
                .tid("testTid2")
                .build();

        payment.applyPaymentApprovalResult(vo);

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.APPROVED),
                () -> assertThat(payment.getTransactionId()).isEqualTo("testTid2")
        );
    }

    @Test
    @DisplayName("applyPaymentCancelResult() 호출 시 취소 결과가 반영된다.")
    void applyPaymentCancelResult() {
        InicisPayment payment = buildInicisPayment();
        InicisPaymentCancelResVo vo = InicisPaymentCancelResVo.builder()
                .resultMessage("결제 취소 완료")
                .reason("테스트 취소 사유")
                .cancelDateTime(LocalDateTime.now())
                .build();

        payment.applyPaymentCancelResult(vo);

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED),
                () -> assertThat(payment.getPaymentCancelList()).hasSize(1),
                () -> assertThat(payment.getPaymentCancelList().get(0).getPaymentCancelType()).isEqualTo(PaymentCancelType.CANCEL),
                () -> assertThat(payment.getPaymentCancelList().get(0).getCancelAmount()).isEqualTo(10000L),
                () -> assertThat(payment.getPaymentLogList()).hasSize(2)
        );
    }

    @Test
    @DisplayName("applyPaymentNetworkCancelResult() 호출 시 망취소 결과가 반영된다.")
    void applyPaymentNetworkCancelResult() {
        InicisPayment payment = buildInicisPayment();
        InicisPaymentNetworkCancelResVo vo = InicisPaymentNetworkCancelResVo.builder()
                .resultMessage("결제 망취소 완료")
                .tid("testTid")
                .build();

        payment.applyPaymentNetworkCancelResult(vo);

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.NETWORK_CANCELED),
                () -> assertThat(payment.getPaymentCancelList()).hasSize(1),
                () -> assertThat(payment.getPaymentCancelList().get(0).getPaymentCancelType()).isEqualTo(PaymentCancelType.NETWORK_CANCEL),
                () -> assertThat(payment.getPaymentLogList()).hasSize(2)
        );
    }

    @Test
    @DisplayName("applyPaymentFail() 호출 시 실패 상태로 변경된다.")
    void applyPaymentFail() {
        InicisPayment payment = buildInicisPayment();

        payment.applyPaymentFail(PaymentStatus.DECLINED, "결제 승인 실패");

        assertAll(
                () -> assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.DECLINED),
                () -> assertThat(payment.getPaymentLogList()).hasSize(2)
        );
    }

    @Test
    @DisplayName("transactionId 가 있는 경우 생성 시 설정된다.")
    void inicisPayment_withTransactionId() {
        InicisPaymentCreateCommand cmd = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .transactionId("existingTxId")
                .authToken("testAuthToken")
                .idcCode("testIdcCode")
                .approvalUrl("http://approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment("testPaymentNo3", cmd);

        assertThat(payment.getTransactionId()).isEqualTo("existingTxId");
    }
}
