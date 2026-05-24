package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.configuration.MapStructConfig;
import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.interfaces.rest.dto.PaymentListResDto;
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

    PaymentListResDto mapToDto(PaymentListResVo paymentListResVo);
}
