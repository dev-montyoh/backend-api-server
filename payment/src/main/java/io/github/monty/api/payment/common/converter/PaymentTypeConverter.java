package io.github.monty.api.payment.common.converter;

import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentGatewayType, String> {
    @Override
    public String convertToDatabaseColumn(PaymentGatewayType attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;
    }

    @Override
    public PaymentGatewayType convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PaymentGatewayType.fromCode(dbData) : null;
    }
}
