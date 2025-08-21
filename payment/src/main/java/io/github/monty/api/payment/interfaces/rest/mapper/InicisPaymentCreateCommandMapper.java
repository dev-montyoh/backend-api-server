package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCreateResultVO;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateRequest;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentCreateCommandMapper {

    @Mapping(target = "paymentGatewayType", source = "paymentGatewayType")
    InicisPaymentCreateCommand mapToCommand(InicisPaymentCreateRequest inicisPaymentCreateRequest, PaymentGatewayType paymentGatewayType);

    InicisPaymentCreateResponse mapToDTO(InicisPaymentCreateResultVO inicisPaymentCreateResultVO);
}
