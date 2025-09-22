package io.github.monty.api.payment.common.converter;

import io.github.monty.api.payment.common.constants.PaymentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    @Override
    public String convertToDatabaseColumn(PaymentStatus attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;
    }

    @Override
    public PaymentStatus convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PaymentStatus.fromCode(dbData) : null;
    }
}
