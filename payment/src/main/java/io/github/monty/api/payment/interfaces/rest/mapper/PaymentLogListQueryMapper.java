package io.github.monty.api.payment.interfaces.rest.mapper;

import io.github.monty.api.payment.common.configuration.MapStructConfig;
import io.github.monty.api.payment.domain.model.query.PaymentLogListQuery;
import io.github.monty.api.payment.domain.model.vo.PaymentLogListResVo;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentLogListResDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.PageRequest;

@Mapper(config = MapStructConfig.class)
public interface PaymentLogListQueryMapper {

    @Mapping(target = "pageable", ignore = true)
    PaymentLogListQuery mapToQuery(String paymentNo, Long page, Long size);

    @AfterMapping
    default void mapToQuery(@MappingTarget PaymentLogListQuery.PaymentLogListQueryBuilder builder, Long page, Long size) {
        PageRequest pageRequest = PageRequest.of(page.intValue() - 1, size.intValue());
        builder.pageable(pageRequest);
    }

    PaymentLogListResDto mapToDto(PaymentLogListResVo paymentLogListResVo);
}
