package io.github.monty.api.payment.infrastructure.jpa.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.aggregate.PaymentCancel;
import io.github.monty.api.payment.domain.model.vo.PaymentListResVo;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface PaymentListMapper {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "paymentList", source = "paymentList", qualifiedByName = "PaymentListResultVO.Payment")
    PaymentListResVo mapToVo(List<Payment> paymentList, Long totalPages, Long totalCount);

    @Named("PaymentListResultVO.Payment")
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "approvalDateTime", ignore = true)
    @Mapping(target = "paymentServiceProviderType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "cancelAmount", ignore = true)
    PaymentListResVo.Payment mapToVoPayment(Payment payment);

    @AfterMapping
    default void mapToVoPayment(@MappingTarget PaymentListResVo.Payment.PaymentBuilder builder, Payment payment) {
        builder.paymentStatus(payment.getPaymentStatus().getDescription());
        if (!ObjectUtils.isEmpty(payment.getApprovalDateTime())) {
            builder.approvalDateTime(payment.getApprovalDateTime().format(dateTimeFormatter));
        }
        builder.paymentServiceProviderType(payment.getPaymentServiceProviderType().getCode());
        builder.createdAt(payment.getCreatedAt().format(dateTimeFormatter));
        //  총 취소 금액
        long totalCancelAmount = payment.getPaymentCancelList().stream()
                .mapToLong(PaymentCancel::getCancelAmount)
                .sum();
        builder.cancelAmount(totalCancelAmount);
    }
}
