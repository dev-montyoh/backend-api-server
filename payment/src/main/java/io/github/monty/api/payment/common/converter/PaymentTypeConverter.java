package io.github.monty.api.payment.common.converter;

import io.github.monty.api.payment.common.constants.PaymentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentType, String> {
    @Override
    public String convertToDatabaseColumn(PaymentType attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;
    }

    @Override
    public PaymentType convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PaymentType.fromCode(dbData) : null;
    }
}
