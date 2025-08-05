package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.PaymentInfoQuery;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PaymentInfoQueryMapper {

    PaymentInfoQuery mapToQuery(String oid, String price, PaymentType paymentType);
}
