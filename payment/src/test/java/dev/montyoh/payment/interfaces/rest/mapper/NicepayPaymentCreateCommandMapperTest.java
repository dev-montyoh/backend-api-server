package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentCreateResVo;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NicepayPaymentCreateCommandMapperTest {

    private final NicepayPaymentCreateCommandMapper mapper = Mappers.getMapper(NicepayPaymentCreateCommandMapper.class);

    @Test
    @DisplayName("NicepayPaymentCreateReqDto 를 NicepayPaymentCreateCommand 로 변환한다.")
    void mapToCommand() {
        //  given
        NicepayPaymentCreateReqDto reqDto = new NicepayPaymentCreateReqDto(
                "testOrderNo", "testAuthToken", "testSignature", "testTransactionId",
                "http://approval.url", "http://network-cancel.url", 10000L
        );

        //  when
        NicepayPaymentCreateCommand actual = mapper.mapToCommand(reqDto, PgProviderType.NICEPAY);

        //  then
        assertAll(
                () -> assertThat(actual.getPgProviderType()).isEqualTo(PgProviderType.NICEPAY),
                () -> assertThat(actual.getOrderNo()).isEqualTo(reqDto.orderNo()),
                () -> assertThat(actual.getPrice()).isEqualTo(reqDto.price()),
                () -> assertThat(actual.getAuthToken()).isEqualTo(reqDto.authToken()),
                () -> assertThat(actual.getSignature()).isEqualTo(reqDto.signature()),
                () -> assertThat(actual.getNextApprovalUrl()).isEqualTo(reqDto.nextApprovalUrl()),
                () -> assertThat(actual.getNetworkCancelUrl()).isEqualTo(reqDto.networkCancelUrl())
        );
    }

    @Test
    @DisplayName("NicepayPaymentCreateResVo 를 NicepayPaymentCreateResDto 로 변환한다.")
    void mapToDto() {
        //  given
        NicepayPaymentCreateResVo resVo = NicepayPaymentCreateResVo.builder()
                .paymentNo("testPaymentNo")
                .build();

        //  when
        NicepayPaymentCreateResDto actual = mapper.mapToDto(resVo);

        //  then
        assertThat(actual.paymentNo()).isEqualTo(resVo.getPaymentNo());
    }
}
