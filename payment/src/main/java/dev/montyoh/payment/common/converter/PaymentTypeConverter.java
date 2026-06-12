package dev.montyoh.payment.common.converter;

import dev.montyoh.payment.common.constants.PgProviderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PgProviderType, String> {
    @Override
    public String convertToDatabaseColumn(PgProviderType attribute) {
        return !ObjectUtils.isEmpty(attribute) ? attribute.getCode() : null;
    }

    @Override
    public PgProviderType convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? PgProviderType.fromCode(dbData) : null;
    }
}
