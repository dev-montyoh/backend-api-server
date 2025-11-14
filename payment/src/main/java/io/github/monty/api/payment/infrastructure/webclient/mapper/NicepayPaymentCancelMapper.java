package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCancelReqVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCancelResVo;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentCancelRequest;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentCancelResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentCancelMapper {

    @Mapping(target = "TID", source = "nicepayPaymentCancelReqVo.transactionId")
    @Mapping(target = "MID", source = "nicepayPaymentCancelReqVo.mid")
    @Mapping(target = "Moid", source = "nicepayPaymentCancelReqVo.orderNo")
    @Mapping(target = "CancelAmt", source = "nicepayPaymentCancelReqVo.cancelAmount")
    @Mapping(target = "CancelMsg", source = "nicepayPaymentCancelReqVo.cancelReason")
    @Mapping(target = "PartialCancelCode", constant = "0")
    @Mapping(target = "EdiDate", source = "nicepayPaymentCancelReqVo.ediDate")
    @Mapping(target = "SignData", source = "nicepayPaymentCancelReqVo.signData")
    @Mapping(target = "CharSet", source = "charSet")
    @Mapping(target = "EdiType", source = "dataFormat")
    @Mapping(target = "MallReserved", source = "nicepayPaymentCancelReqVo.mallReserved")
    @Mapping(target = "RefundAcctNo", ignore = true)
    @Mapping(target = "RefundBankCd", ignore = true)
    @Mapping(target = "RefundAcctNm", ignore = true)
    NicepayPaymentCancelRequest mapToDto(NicepayPaymentCancelReqVo nicepayPaymentCancelReqVo, String dataFormat, String charSet);

    @Mapping(target = "resultMessage", source = "nicepayPaymentCancelResponse.resultMsg")
    @Mapping(target = "cancelDateTime", ignore = true)
    @Mapping(target = "reason", source = "cancelReason")
    @Mapping(target = "cancelAmount", source = "nicepayPaymentCancelResponse.cancelAmt")
    @Mapping(target = "signature", source = "nicepayPaymentCancelResponse.signature")
    @Mapping(target = "cancelTransactionId", source = "nicepayPaymentCancelResponse.tid")
    NicepayPaymentCancelResVo mapToVo(NicepayPaymentCancelResponse nicepayPaymentCancelResponse, String cancelReason);

    @AfterMapping
    default void mapToVo(@MappingTarget NicepayPaymentCancelResVo.NicepayPaymentCancelResVoBuilder<?, ?> builder, NicepayPaymentCancelResponse nicepayPaymentCancelResponse) {
        if (ObjectUtils.isEmpty(nicepayPaymentCancelResponse.getCancelDate()) || ObjectUtils.isEmpty(nicepayPaymentCancelResponse.getCancelDate())) {
            return;
        }
        String dateTime = nicepayPaymentCancelResponse.getCancelDate() + nicepayPaymentCancelResponse.getCancelTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        builder.cancelDateTime(LocalDateTime.parse(dateTime, formatter));
    }
}
