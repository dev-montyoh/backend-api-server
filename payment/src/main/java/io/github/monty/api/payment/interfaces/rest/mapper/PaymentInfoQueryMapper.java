package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.InisysPaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.vo.InisysPaymentAuthInfoVO;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentAuthInfoResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper(config = MapStructConfig.class)
public interface PaymentInfoQueryMapper {

    InisysPaymentAuthInfoQuery mapToQuery(String oid, String price, PaymentType paymentType);

    @Mapping(target = "signature", source = "signature")
    @Mapping(target = "verification", source = "verification")
    @Mapping(target = "mKey", ignore = true)
    @Mapping(target = "mid", source = "mid")
    InisysPaymentAuthInfoResponse mapToDTO(InisysPaymentAuthInfoVO inisysPaymentAuthInfoVO);

    @AfterMapping
    default void mapToDTO(@MappingTarget InisysPaymentAuthInfoResponse.InisysPaymentAuthInfoResponseBuilder builder, InisysPaymentAuthInfoVO inisysPaymentAuthInfoVO) {
        builder.mKey(inisysPaymentAuthInfoVO.getMKey());
    }
}
