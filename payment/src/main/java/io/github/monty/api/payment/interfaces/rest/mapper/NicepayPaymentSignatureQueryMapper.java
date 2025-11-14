package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.query.NicepayPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentSignatureResDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentSignatureQueryMapper {

    NicepayPaymentSignatureQuery mapToQuery(String price, PaymentServiceProviderType paymentServiceProviderType);

    NicepayPaymentSignatureResDto mapToDto(NicepayPaymentSignatureResVo nicepayPaymentSignatureResVo);
}
