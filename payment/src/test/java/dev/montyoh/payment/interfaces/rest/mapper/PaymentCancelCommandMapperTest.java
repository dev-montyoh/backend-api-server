package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.interfaces.rest.dto.PaymentCancelReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentCancelCommandMapperTest {

    private final PaymentCancelCommandMapper mapper = Mappers.getMapper(PaymentCancelCommandMapper.class);

    @Test
    @DisplayName("PaymentCancelReqDto 와 paymentNo 를 PaymentCancelCommand 로 변환한다.")
    void mapToCommand() {
        //  given
        PaymentCancelReqDto reqDto = new PaymentCancelReqDto("테스트 취소 사유");
        String paymentNo = "testPaymentNo";

        //  when
        PaymentCancelCommand actual = mapper.mapToCommand(reqDto, paymentNo);

        //  then
        assertAll(
                () -> assertThat(actual.getPaymentNo()).isEqualTo(paymentNo),
                () -> assertThat(actual.getCancelReason()).isEqualTo(reqDto.cancelReason())
        );
    }
}
