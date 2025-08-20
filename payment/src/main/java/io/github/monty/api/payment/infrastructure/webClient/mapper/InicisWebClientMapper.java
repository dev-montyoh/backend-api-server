package io.github.monty.api.payment.infrastructure.webClient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApproveRequestVO;
import io.github.monty.api.payment.infrastructure.webClient.dto.InicisPaymentApproveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisWebClientMapper {

    @Mapping(target = "charset", source = "charset")
    @Mapping(target = "format", source = "format")
    InicisPaymentApproveRequest mapToDto(InicisPaymentApproveRequestVO inicisPaymentApproveRequestVO, String charset, String format);
}
