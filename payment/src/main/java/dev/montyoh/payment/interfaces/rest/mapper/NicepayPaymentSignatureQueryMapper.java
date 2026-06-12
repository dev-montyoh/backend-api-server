package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.query.NicepayPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentSignatureResDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface NicepayPaymentSignatureQueryMapper {

    NicepayPaymentSignatureQuery mapToQuery(String price, PgProviderType pgProviderType);

    NicepayPaymentSignatureResDto mapToDto(NicepayPaymentSignatureResVo nicepayPaymentSignatureResVo);
}
