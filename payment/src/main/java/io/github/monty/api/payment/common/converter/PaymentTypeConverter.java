package io.github.monty.api.payment.common.converter;

import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentServiceProviderType, String> {
    @Override
    public String convertToDatabaseColumn(PaymentServiceProviderType attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;
    }

    @Override
    public PaymentServiceProviderType convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PaymentServiceProviderType.fromCode(dbData) : null;
    }
}
