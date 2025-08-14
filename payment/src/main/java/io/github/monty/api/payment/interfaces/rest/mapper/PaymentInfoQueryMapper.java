package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.InisysPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.InisysPaymentSignatureVO;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentSignatureResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface PaymentInfoQueryMapper {

    InisysPaymentSignatureQuery mapToQuery(String oid, String price, PaymentType paymentType);

    @Mapping(target = "signature", source = "signature")
    @Mapping(target = "verification", source = "verification")
    @Mapping(target = "mKey", ignore = true)
    @Mapping(target = "mid", source = "mid")
    InisysPaymentSignatureResponse mapToDTO(InisysPaymentSignatureVO inisysPaymentSignatureVO);

    @AfterMapping
    default void mapToDTO(@MappingTarget InisysPaymentSignatureResponse.InisysPaymentAuthInfoResponseBuilder builder, InisysPaymentSignatureVO inisysPaymentSignatureVO) {
        builder.mKey(inisysPaymentSignatureVO.getMKey());
    }
}
