package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.interfaces.rest.dto.PaymentCancelReqDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PaymentCancelCommandMapper {

    @Mapping(target = "cancelReason", source = "paymentCancelReqDto.cancelReason")
    PaymentCancelCommand mapToCommand(PaymentCancelReqDto paymentCancelReqDto, String paymentNo);
}
