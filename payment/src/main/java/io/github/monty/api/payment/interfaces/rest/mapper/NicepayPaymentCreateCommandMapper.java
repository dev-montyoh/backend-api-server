package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.NicepayPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCreateResVo;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentCreateCommandMapper {

    @Mapping(target = "paymentServiceProviderType", source = "paymentServiceProviderType")
    NicepayPaymentCreateCommand mapToCommand(NicepayPaymentCreateReqDto nicepayPaymentCreateReqDto, PaymentServiceProviderType paymentServiceProviderType);

    NicepayPaymentCreateResDto mapToDto(NicepayPaymentCreateResVo nicepayPaymentCreateResVo);
}
