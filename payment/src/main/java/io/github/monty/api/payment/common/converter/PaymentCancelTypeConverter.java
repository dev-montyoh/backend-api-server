package io.github.monty.api.payment.common.converter;

import io.github.monty.api.payment.common.constants.PaymentCancelType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentCancelTypeConverter implements AttributeConverter<PaymentCancelType, String> {
    @Override
    public String convertToDatabaseColumn(PaymentCancelType attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;

    }

    @Override
    public PaymentCancelType convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PaymentCancelType.fromCode(dbData) : null;
    }
}
