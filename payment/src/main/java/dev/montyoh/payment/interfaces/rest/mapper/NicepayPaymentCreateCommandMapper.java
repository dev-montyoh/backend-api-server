package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentCreateResVo;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentCreateCommandMapper {

    @Mapping(target = "pgProviderType", source = "pgProviderType")
    NicepayPaymentCreateCommand mapToCommand(NicepayPaymentCreateReqDto nicepayPaymentCreateReqDto, PgProviderType pgProviderType);

    NicepayPaymentCreateResDto mapToDto(NicepayPaymentCreateResVo nicepayPaymentCreateResVo);
}
