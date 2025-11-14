package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCreateResVo;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateReqDto;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentCreateCommandMapper {

    @Mapping(target = "paymentServiceProviderType", source = "paymentServiceProviderType")
    @Mapping(target = "transactionId", ignore = true)
    InicisPaymentCreateCommand mapToCommand(InicisPaymentCreateReqDto inicisPaymentCreateReqDto, PaymentServiceProviderType paymentServiceProviderType);

    InicisPaymentCreateResDto mapToDTO(InicisPaymentCreateResVo inicisPaymentCreateResVo);
}
