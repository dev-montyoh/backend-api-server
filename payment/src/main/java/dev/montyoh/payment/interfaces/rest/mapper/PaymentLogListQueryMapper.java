package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.interfaces.rest.dto.PaymentLogListResDto;
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
