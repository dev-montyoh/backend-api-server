package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.query.PaymentListQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentListResultVO;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentListResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.PageRequest;

@Mapper(config = MapStructConfig.class)
public interface PaymentListQueryMapper {

    @Mapping(target = "pageable", ignore = true)
    PaymentListQuery mapToQuery(Long page, Long size);

    @AfterMapping
    default void mapToQuery(@MappingTarget PaymentListQuery.PaymentListQueryBuilder builder, Long page, Long size) {
        PageRequest pageRequest = PageRequest.of(page.intValue() - 1, size.intValue());
        builder.pageable(pageRequest);
    }

    PaymentListResponse mapToDto(PaymentListResultVO paymentListResultVO);
}
