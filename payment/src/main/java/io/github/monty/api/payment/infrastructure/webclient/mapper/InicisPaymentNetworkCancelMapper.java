package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResVo;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentNetworkCancelMapper {

    @Mapping(target = "resultMessage", source = "inicisPaymentNetworkCancelResponse.resultMsg")
    @Mapping(target = "tid", source = "inicisPaymentNetworkCancelResponse.tid")
    InicisPaymentNetworkCancelResVo mapToVo(InicisPaymentNetworkCancelResponse inicisPaymentNetworkCancelResponse);
}
