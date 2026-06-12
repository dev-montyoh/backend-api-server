package dev.montyoh.payment.domain.service;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.aggregate.Payment;
import dev.montyoh.payment.domain.model.aggregate.PaymentCancel;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.repository.PaymentCustomRepository;
import dev.montyoh.payment.domain.repository.PaymentRepository;
import dev.montyoh.payment.infrastructure.jpa.mapper.PaymentListMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    PaymentCustomRepository paymentCustomRepository;
    @Mock
    PaymentListMapper paymentListMapper;

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
    @DisplayName("결제 목록을 조회한다.")
    void requestPaymentList_success() {
        //  given
        PageRequest pageable = PageRequest.of(0, 50);
        PaymentListQuery query = PaymentListQuery.builder().pageable(pageable).build();
        InicisPayment payment = buildInicisPayment();
        Page<Payment> page = new PageImpl<>(List.of(payment), pageable, 1);
        PaymentListResVo resVo = PaymentListResVo.builder().paymentList(List.of()).totalPages(1L).totalCount(1L).build();
        given(paymentRepository.findAllByOrderByCreatedAtDesc(pageable)).willReturn(page);
        given(paymentListMapper.mapToVo(any(), any(), any())).willReturn(resVo);

        //  when
        PaymentListResVo actual = paymentService.requestPaymentList(query);

        //  then
        assertThat(actual).isEqualTo(resVo);
    }

    @Test
    @DisplayName("결제 승인 요청 상태로 변경한다.")
    void markAsRequested_success() {
        //  given
        InicisPayment payment = buildInicisPayment();
        given(paymentRepository.findByPaymentNo("testPaymentNo")).willReturn(Optional.of(payment));
        given(paymentRepository.save(any())).willReturn(payment);

        //  when, then (no exception)
        paymentService.markAsRequested("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 markAsRequested 호출 시 예외가 발생한다.")
    void markAsRequested_notFound() {
        //  given
        given(paymentRepository.findByPaymentNo("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> paymentService.markAsRequested("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("취소 가능한 결제를 검증한다.")
    void isEnableCancel_success() {
        //  given
        InicisPayment payment = buildInicisPayment();
        given(paymentCustomRepository.findByPaymentNoWithCancelList("testPaymentNo")).willReturn(Optional.of(payment));

        //  when, then (no exception)
        paymentService.isEnableCancel("testPaymentNo");
    }

    @Test
    @DisplayName("존재하지 않는 결제번호로 isEnableCancel 호출 시 예외가 발생한다.")
    void isEnableCancel_notFound() {
        //  given
        given(paymentCustomRepository.findByPaymentNoWithCancelList("invalid")).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() -> paymentService.isEnableCancel("invalid"))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("전액 취소된 결제에 대해 isEnableCancel 호출 시 예외가 발생한다.")
    void isEnableCancel_alreadyCancelled() {
        //  given
        InicisPayment payment = buildInicisPayment();
        PaymentCancel cancel = new PaymentCancel(payment, 10000L, "취소 사유", dev.montyoh.payment.common.constants.PaymentCancelType.CANCEL);
        payment.getPaymentCancelList().add(cancel);
        given(paymentCustomRepository.findByPaymentNoWithCancelList("testPaymentNo")).willReturn(Optional.of(payment));

        //  when, then
        assertThatThrownBy(() -> paymentService.isEnableCancel("testPaymentNo"))
                .isInstanceOf(ApplicationException.class);
    }
}
