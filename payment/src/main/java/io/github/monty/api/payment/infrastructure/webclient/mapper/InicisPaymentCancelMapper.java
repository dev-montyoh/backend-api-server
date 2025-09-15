package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCancelResultVO;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentCancelResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentCancelMapper {

    @Mapping(target = "resultMessage", source = "inicisPaymentCancelResponse.resultMsg")
    @Mapping(target = "cancelDateTime", ignore = true)
    @Mapping(target = "cashReceiptCancelNo", source = "inicisPaymentCancelResponse.cshrCancelNum")
    InicisPaymentCancelResultVO mapToVo(InicisPaymentCancelResponse inicisPaymentCancelResponse, boolean isCancelled, String reason);

    @AfterMapping
    default void afterMapping(@MappingTarget InicisPaymentCancelResultVO.InicisPaymentCancelResultVOBuilder<InicisPaymentCancelResultVO, ?> builder,
                              InicisPaymentCancelResponse inicisPaymentCancelResponse) {
        String dateTimeString = inicisPaymentCancelResponse.getCancelDate() + inicisPaymentCancelResponse.getCancelTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        builder.cancelDateTime(dateTime);
    }
}
