package dev.montyoh.payment.infrastructure.webclient.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.vo.InicisPaymentNetworkCancelResVo;
import dev.montyoh.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentNetworkCancelMapper {

    @Mapping(target = "resultMessage", source = "inicisPaymentNetworkCancelResponse.resultMsg")
    @Mapping(target = "tid", source = "inicisPaymentNetworkCancelResponse.tid")
    InicisPaymentNetworkCancelResVo mapToVo(InicisPaymentNetworkCancelResponse inicisPaymentNetworkCancelResponse);
}
