package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.common.constants.PaymentServiceProviderType;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentCreateResVo;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentCreateCommandMapper {

    @Mapping(target = "paymentServiceProviderType", source = "paymentServiceProviderType")
    NicepayPaymentCreateCommand mapToCommand(NicepayPaymentCreateReqDto nicepayPaymentCreateReqDto, PaymentServiceProviderType paymentServiceProviderType);

    NicepayPaymentCreateResDto mapToDto(NicepayPaymentCreateResVo nicepayPaymentCreateResVo);
}
