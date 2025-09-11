package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResultVO;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentNetworkCancelMapper {

    @Mapping(target = "resultMessage", source = "inicisPaymentNetworkCancelResponse.resultMsg")
    @Mapping(target = "timestamp", source = "inicisPaymentNetworkCancelResponse.timestamp")
    @Mapping(target = "tid", source = "inicisPaymentNetworkCancelResponse.tid")
    @Mapping(target = "mid", source = "inicisPaymentNetworkCancelResponse.mid")
    @Mapping(target = "moid", source = "inicisPaymentNetworkCancelResponse.moid")
    @Mapping(target = "selectPayMethod", source = "inicisPaymentNetworkCancelResponse.selectPayMethod")
    InicisPaymentNetworkCancelResultVO mapToVo(InicisPaymentNetworkCancelResponse inicisPaymentNetworkCancelResponse, boolean isNetworkCanceled);
}
