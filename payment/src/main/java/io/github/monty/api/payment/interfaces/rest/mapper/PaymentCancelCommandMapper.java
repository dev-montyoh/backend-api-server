package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentCancelRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config =  MapStructConfig.class)
public interface PaymentCancelCommandMapper {

    @Mapping(target = "cancelReason", source = "paymentCancelRequest.cancelReason")
    PaymentCancelCommand mapToCommand(PaymentCancelRequest paymentCancelRequest, String paymentNo);
}
