package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCreateResVo;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class InicisPaymentCreateCommandMapperTest {

    private final InicisPaymentCreateCommandMapper mapper = Mappers.getMapper(InicisPaymentCreateCommandMapper.class);

    @Test
    @DisplayName("InicisPaymentCreateReqDto 를 InicisPaymentCreateCommand 로 변환한다.")
    void mapToCommand() {
        //  given
        InicisPaymentCreateReqDto reqDto = new InicisPaymentCreateReqDto(
                "testMid", "testOrderNo", "testAuthToken", "testIdcCode",
                "http://approval.url", "http://network-cancel.url", 10000L
        );

        //  when
        InicisPaymentCreateCommand actual = mapper.mapToCommand(reqDto, PgProviderType.INICIS);

        //  then
        assertAll(
                () -> assertThat(actual.getPgProviderType()).isEqualTo(PgProviderType.INICIS),
                () -> assertThat(actual.getOrderNo()).isEqualTo(reqDto.orderNo()),
                () -> assertThat(actual.getPrice()).isEqualTo(reqDto.price()),
                () -> assertThat(actual.getAuthToken()).isEqualTo(reqDto.authToken()),
                () -> assertThat(actual.getIdcCode()).isEqualTo(reqDto.idcCode()),
                () -> assertThat(actual.getApprovalUrl()).isEqualTo(reqDto.approvalUrl()),
                () -> assertThat(actual.getNetworkCancelUrl()).isEqualTo(reqDto.networkCancelUrl()),
                () -> assertThat(actual.getTransactionId()).isNull()
        );
    }

    @Test
    @DisplayName("InicisPaymentCreateResVo 를 InicisPaymentCreateResDto 로 변환한다.")
    void mapToDTO() {
        //  given
        InicisPaymentCreateResVo resVo = InicisPaymentCreateResVo.builder()
                .paymentNo("testPaymentNo")
                .build();

        //  when
        InicisPaymentCreateResDto actual = mapper.mapToDTO(resVo);

        //  then
        assertThat(actual.paymentNo()).isEqualTo(resVo.getPaymentNo());
    }
}
