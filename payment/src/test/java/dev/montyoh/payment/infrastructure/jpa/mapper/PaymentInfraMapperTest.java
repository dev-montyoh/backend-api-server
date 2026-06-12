package dev.montyoh.payment.infrastructure.jpa.mapper;

import dev.montyoh.payment.common.constants.PaymentStatus;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.entity.PaymentLog;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentInfraMapperTest {

    private final PaymentListMapper paymentListMapper = Mappers.getMapper(PaymentListMapper.class);
    private final PaymentLogListMapper paymentLogListMapper = Mappers.getMapper(PaymentLogListMapper.class);

    private InicisPayment buildPayment() {
        InicisPaymentCreateCommand cmd = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo").price(10000L)
                .authToken("testAuthToken").idcCode("testIdcCode")
                .approvalUrl("http://approval.url").networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment("testPaymentNo", cmd);
        ReflectionTestUtils.setField(payment, "createdAt", LocalDateTime.of(2026, 6, 12, 0, 0, 0));
        return payment;
    }

    @Test
    @DisplayName("Payment 목록을 PaymentListResVo로 변환한다.")
    void paymentList_mapToVo() {
        //  given
        InicisPayment payment = buildPayment();

        //  when
        PaymentListResVo actual = paymentListMapper.mapToVo(List.of(payment), 1L, 1L);

        //  then
        assertAll(
                () -> assertThat(actual.totalPages()).isEqualTo(1L),
                () -> assertThat(actual.totalCount()).isEqualTo(1L),
                () -> assertThat(actual.paymentList()).hasSize(1),
                () -> assertThat(actual.paymentList().get(0).paymentNo()).isEqualTo("testPaymentNo"),
                () -> assertThat(actual.paymentList().get(0).createdAt()).isEqualTo("2026-06-12 00:00:00")
        );
    }

    @Test
    @DisplayName("빈 Payment 목록을 PaymentListResVo로 변환한다.")
    void paymentList_mapToVo_empty() {
        //  when
        PaymentListResVo actual = paymentListMapper.mapToVo(List.of(), 0L, 0L);

        //  then
        assertAll(
                () -> assertThat(actual.totalPages()).isEqualTo(0L),
                () -> assertThat(actual.paymentList()).isEmpty()
        );
    }

    @Test
    @DisplayName("PaymentLog 목록을 PaymentLogListResVo로 변환한다.")
    void paymentLogList_mapToVo() {
        //  given
        InicisPayment payment = buildPayment();
        PaymentLog paymentLog = new PaymentLog(payment, PaymentStatus.AUTHENTICATED, "인증 완료");
        ReflectionTestUtils.setField(paymentLog, "createdAt", LocalDateTime.of(2026, 6, 12, 0, 0, 0));

        //  when
        PaymentLogListResVo actual = paymentLogListMapper.mapToVo(List.of(paymentLog), 1L, 1L);

        //  then
        assertAll(
                () -> assertThat(actual.totalPages()).isEqualTo(1L),
                () -> assertThat(actual.paymentLogList()).hasSize(1),
                () -> assertThat(actual.paymentLogList().get(0).createdAt()).isEqualTo("2026-06-12 00:00:00"),
                () -> assertThat(actual.paymentLogList().get(0).paymentStatus()).isEqualTo("인증 완료")
        );
    }

    @Test
    @DisplayName("빈 PaymentLog 목록을 PaymentLogListResVo로 변환한다.")
    void paymentLogList_mapToVo_empty() {
        //  when
        PaymentLogListResVo actual = paymentLogListMapper.mapToVo(List.of(), 0L, 0L);

        //  then
        assertAll(
                () -> assertThat(actual.totalPages()).isEqualTo(0L),
                () -> assertThat(actual.paymentLogList()).isEmpty()
        );
    }
}
