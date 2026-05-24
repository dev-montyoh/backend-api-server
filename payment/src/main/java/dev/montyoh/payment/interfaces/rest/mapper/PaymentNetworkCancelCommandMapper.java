package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.command.PaymentNetworkCancelCommand;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PaymentNetworkCancelCommandMapper {

    PaymentNetworkCancelCommand mapToCommand(String paymentNo);
}
