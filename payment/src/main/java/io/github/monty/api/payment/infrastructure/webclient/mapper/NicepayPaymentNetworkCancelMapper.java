package io.github.monty.api.payment.infrastructure.webclient.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentNetworkCancelReqVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentNetworkCancelResVo;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentNetworkCancelRequest;
import io.github.monty.api.payment.infrastructure.webclient.dto.NicepayPaymentNetworkCancelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentNetworkCancelMapper {

    @Mapping(target = "TID", source = "nicepayPaymentNetworkCancelReqVo.transactionId")
    @Mapping(target = "AuthToken", source = "nicepayPaymentNetworkCancelReqVo.authToken")
    @Mapping(target = "MID", source = "nicepayPaymentNetworkCancelReqVo.mid")
    @Mapping(target = "Amt", source = "nicepayPaymentNetworkCancelReqVo.amount")
    @Mapping(target = "EdiDate", source = "nicepayPaymentNetworkCancelReqVo.ediDate")
    @Mapping(target = "EdiType", source = "dataFormat")
    @Mapping(target = "CharSet", source = "charSet")
    @Mapping(target = "SignData", source = "nicepayPaymentNetworkCancelReqVo.signData")
    @Mapping(target = "MallReserved", source = "nicepayPaymentNetworkCancelReqVo.mallReserved")
    @Mapping(target = "NetCancel", constant = "1")
    NicepayPaymentNetworkCancelRequest mapToDto(NicepayPaymentNetworkCancelReqVo nicepayPaymentNetworkCancelReqVo, String dataFormat, String charSet, String cancelMessage);

    @Mapping(target = "resultMessage", source = "resultMsg")
    @Mapping(target = "tid", source = "tid")
    @Mapping(target = "cancelAmount", source = "cancelAmt")
    @Mapping(target = "signature", source = "signature")
    NicepayPaymentNetworkCancelResVo mapToVo(NicepayPaymentNetworkCancelResponse nicepayPaymentNetworkCancelResponse);
}
