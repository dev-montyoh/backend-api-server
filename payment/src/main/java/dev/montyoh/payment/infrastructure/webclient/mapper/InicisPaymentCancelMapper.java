package dev.montyoh.payment.infrastructure.webclient.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCancelResVo;
import dev.montyoh.payment.infrastructure.webclient.dto.InicisPaymentCancelResponse;
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
    InicisPaymentCancelResVo mapToVo(InicisPaymentCancelResponse inicisPaymentCancelResponse, String reason);

    @AfterMapping
    default void afterMapping(@MappingTarget InicisPaymentCancelResVo.InicisPaymentCancelResVoBuilder builder,
                              InicisPaymentCancelResponse inicisPaymentCancelResponse) {
        String dateTimeString = inicisPaymentCancelResponse.getCancelDate() + inicisPaymentCancelResponse.getCancelTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        builder.cancelDateTime(dateTime);
    }
}
