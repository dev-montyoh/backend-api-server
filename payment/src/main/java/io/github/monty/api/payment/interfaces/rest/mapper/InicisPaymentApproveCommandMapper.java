package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.InicisPaymentApproveCommand;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentApproveCommandMapper {

    InicisPaymentApproveCommand mapToCommand(String paymentNo, PaymentServiceProviderType PaymentServiceProviderType);
}
