package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentApprovalResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentApprovalMapper {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Mapping(target = "amount", source = "inicisPaymentApprovalResponse.totPrice")
    @Mapping(target = "paymentMethod", source = "inicisPaymentApprovalResponse.payMethod")
    @Mapping(target = "approvalDateTime", ignore = true)
    @Mapping(target = "buyerPhoneNumber", source = "inicisPaymentApprovalResponse.buyerTel")
    @Mapping(target = "resultMessage", source = "inicisPaymentApprovalResponse.resultMsg")
    InicisPaymentApprovalResultVO mapToVo(InicisPaymentApprovalResponse inicisPaymentApprovalResponse, boolean isApproved);

    @AfterMapping
    default void mapToVo(@MappingTarget InicisPaymentApprovalResultVO.InicisPaymentApprovalResultVOBuilder<InicisPaymentApprovalResultVO, ?> builder,
                         InicisPaymentApprovalResponse inicisPaymentApprovalResponse) {
        if (StringUtils.hasText(inicisPaymentApprovalResponse.getApplDate()) && StringUtils.hasText(inicisPaymentApprovalResponse.getApplTime())) {
            LocalDateTime approvalDateTime = LocalDateTime.parse(inicisPaymentApprovalResponse.getApplDate() + inicisPaymentApprovalResponse.getApplTime(), dateTimeFormatter);
            builder.approvalDateTime(approvalDateTime);
        }
    }
}
