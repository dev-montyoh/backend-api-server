package io.github.monty.api.payment.infrastructure.jpa.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.vo.PaymentListResultVO;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface PaymentListMapper {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Mapping(target = "paymentList", source = "paymentList", qualifiedByName = "PaymentListResultVO.Payment")
    PaymentListResultVO mapToVo(List<Payment> paymentList, Long totalPages, Long totalCount);

    @Named("PaymentListResultVO.Payment")
    @Mapping(target = "orderNo", source = "orderNo")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "approvalDateTime", ignore = true)
    @Mapping(target = "paymentServiceProviderType", ignore = true)
    PaymentListResultVO.Payment mapToVoPayment(Payment payment);

    @AfterMapping
    default void mapToVoPayment(@MappingTarget PaymentListResultVO.Payment.PaymentBuilder builder, Payment payment) {
        builder.paymentStatus(payment.getPaymentStatus().getDescription());
        builder.approvalDateTime(payment.getApprovalDateTime().format(dateTimeFormatter));
        builder.paymentServiceProviderType(payment.getPaymentServiceProviderType().getCode());
    }
}
