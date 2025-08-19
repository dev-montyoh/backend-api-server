package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.InicisPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentSignatureResultVO;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentSignatureResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentSignatureQueryMapper {

    InicisPaymentSignatureQuery mapToQuery(String oid, String price, PaymentType paymentType);

    @Mapping(target = "signature", source = "signature")
    @Mapping(target = "verification", source = "verification")
    @Mapping(target = "mKey", ignore = true)
    @Mapping(target = "mid", source = "mid")
    InicisPaymentSignatureResponse mapToDTO(InicisPaymentSignatureResultVO inicisPaymentSignatureResultVO);

    @AfterMapping
    default void mapToDTO(@MappingTarget InicisPaymentSignatureResponse.InicisPaymentSignatureResponseBuilder builder, InicisPaymentSignatureResultVO inicisPaymentSignatureResultVO) {
        builder.mKey(inicisPaymentSignatureResultVO.getMKey());
    }
}
