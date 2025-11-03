package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.command.PaymentNetworkCancelCommand;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PaymentNetworkCancelCommandMapper {

    PaymentNetworkCancelCommand mapToCommand(String paymentNo);
}
