package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.command.InisysPaymentCreateCommand;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InisysPaymentCreateCommandMapper {

    @Mapping(target = "paymentType", source = "paymentType")
    InisysPaymentCreateCommand mapToCommand(InisysPaymentCreateRequest inisysPaymentCreateRequest, PaymentType paymentType);
}
