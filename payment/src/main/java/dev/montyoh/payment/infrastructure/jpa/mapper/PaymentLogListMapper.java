package dev.montyoh.payment.infrastructure.jpa.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.entity.PaymentLog;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface PaymentLogListMapper {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "paymentLogList", source = "paymentLogList", qualifiedByName = "PaymentLogListResultVO.PaymentLog")
    PaymentLogListResVo mapToVo(List<PaymentLog> paymentLogList, Long totalPages, Long totalCount);

    @Named("PaymentLogListResultVO.PaymentLog")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    PaymentLogListResVo.PaymentLog mapToVoPaymentLog(PaymentLog paymentLog);

    @AfterMapping
    default void mapToVoPaymentLog(@MappingTarget PaymentLogListResVo.PaymentLog.PaymentLogBuilder builder, PaymentLog paymentLog) {
        builder.createdAt(paymentLog.getCreatedAt().format(dateTimeFormatter));
        builder.paymentStatus(paymentLog.getPaymentStatus().getDescription());
    }
}
