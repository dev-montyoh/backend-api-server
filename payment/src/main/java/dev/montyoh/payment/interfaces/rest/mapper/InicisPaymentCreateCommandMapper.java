package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.common.constants.PaymentServiceProviderType;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCreateResVo;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentCreateCommandMapper {

    @Mapping(target = "paymentServiceProviderType", source = "paymentServiceProviderType")
    @Mapping(target = "transactionId", ignore = true)
    InicisPaymentCreateCommand mapToCommand(InicisPaymentCreateReqDto inicisPaymentCreateReqDto, PaymentServiceProviderType paymentServiceProviderType);

    InicisPaymentCreateResDto mapToDTO(InicisPaymentCreateResVo inicisPaymentCreateResVo);
}
