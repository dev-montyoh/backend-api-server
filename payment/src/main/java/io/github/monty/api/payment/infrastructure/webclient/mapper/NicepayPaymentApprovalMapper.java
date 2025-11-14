package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentApprovalReqVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentApprovalResVo;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentApprovalRequest;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentApprovalResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentApprovalMapper {

    @Mapping(target = "TID", source = "nicepayPaymentApprovalReqVo.transactionId")
    @Mapping(target = "AuthToken", source = "nicepayPaymentApprovalReqVo.authToken")
    @Mapping(target = "MID", source = "nicepayPaymentApprovalReqVo.mid")
    @Mapping(target = "Amt", source = "nicepayPaymentApprovalReqVo.amount")
    @Mapping(target = "EdiDate", source = "nicepayPaymentApprovalReqVo.ediDate")
    @Mapping(target = "SignData", source = "nicepayPaymentApprovalReqVo.signData")
    @Mapping(target = "EdiType", source = "dataFormat")
    @Mapping(target = "MallReserved", source = "nicepayPaymentApprovalReqVo.mallReserved")
    NicepayPaymentApprovalRequest mapToDto(NicepayPaymentApprovalReqVo nicepayPaymentApprovalReqVo, String dataFormat, String charSet);

    @Mapping(target = "signature", source = "nicepayPaymentApprovalResponse.signature")
    @Mapping(target = "resultMessage", source = "nicepayPaymentApprovalResponse.resultMsg")
    @Mapping(target = "tid", source = "nicepayPaymentApprovalResponse.tid")
    @Mapping(target = "amount", source = "nicepayPaymentApprovalResponse.amt")
    @Mapping(target = "approvalDateTime", ignore = true)
    @Mapping(target = "buyerPhoneNumber", source = "nicepayPaymentApprovalResponse.buyerTel")
    @Mapping(target = "buyerEmail", source = "nicepayPaymentApprovalResponse.buyerEmail")
    @Mapping(target = "paymentMethod", source = "nicepayPaymentApprovalResponse.payMethod")
    NicepayPaymentApprovalResVo mapToVo(NicepayPaymentApprovalResponse nicepayPaymentApprovalResponse);

    @AfterMapping
    default void mapToVo(@MappingTarget NicepayPaymentApprovalResVo.NicepayPaymentApprovalResVoBuilder<?, ?> builder, NicepayPaymentApprovalResponse nicepayPaymentApprovalResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime approvalDateTime = LocalDateTime.parse(nicepayPaymentApprovalResponse.getAuthDate(), formatter);
        builder.approvalDateTime(approvalDateTime);
    }
}
