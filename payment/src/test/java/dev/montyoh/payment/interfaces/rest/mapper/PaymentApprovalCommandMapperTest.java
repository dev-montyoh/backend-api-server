package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.domain.model.command.PaymentApprovalCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentApprovalCommandMapperTest {

    private final PaymentApprovalCommandMapper mapper = Mappers.getMapper(PaymentApprovalCommandMapper.class);

    @Test
    @DisplayName("paymentNo 를 PaymentApprovalCommand 로 변환한다.")
    void mapToCommand() {
        //  given
        String paymentNo = "testPaymentNo";

        //  when
        PaymentApprovalCommand actual = mapper.mapToCommand(paymentNo);

        //  then
        assertThat(actual.getPaymentNo()).isEqualTo(paymentNo);
    }
}
