package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.common.constants.PaymentServiceProviderType;
import dev.montyoh.payment.domain.model.query.InicisPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.InicisPaymentSignatureResVo;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentSignatureResDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface InicisPaymentSignatureQueryMapper {

    InicisPaymentSignatureQuery mapToQuery(String oid, String price, PaymentServiceProviderType paymentServiceProviderType);

    @Mapping(target = "signature", source = "signature")
    @Mapping(target = "verification", source = "verification")
    @Mapping(target = "mKey", ignore = true)
    @Mapping(target = "mid", source = "mid")
    InicisPaymentSignatureResDto mapToDTO(InicisPaymentSignatureResVo inicisPaymentSignatureResVo);

    @AfterMapping
    default void mapToDTO(@MappingTarget InicisPaymentSignatureResDto.InicisPaymentSignatureResDtoBuilder builder, InicisPaymentSignatureResVo inicisPaymentSignatureResVo) {
        builder.mKey(inicisPaymentSignatureResVo.getMKey());
    }
}
