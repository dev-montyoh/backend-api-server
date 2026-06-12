package dev.montyoh.payment.common.converter;

import dev.montyoh.payment.common.constants.PaymentCancelType;
import dev.montyoh.payment.common.constants.PaymentMethod;
import dev.montyoh.payment.common.constants.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentConverterTest {

    private final PaymentStatusConverter paymentStatusConverter = new PaymentStatusConverter();
    private final PaymentCancelTypeConverter paymentCancelTypeConverter = new PaymentCancelTypeConverter();

    @Test
    @DisplayName("PaymentStatus를 DB 컬럼 값으로 변환한다.")
    void paymentStatus_convertToDatabaseColumn() {
        assertAll(
                () -> assertThat(paymentStatusConverter.convertToDatabaseColumn(PaymentStatus.APPROVED)).isEqualTo("APPROVED"),
                () -> assertThat(paymentStatusConverter.convertToDatabaseColumn(null)).isNull()
        );
    }

    @Test
    @DisplayName("DB 컬럼 값을 PaymentStatus로 변환한다.")
    void paymentStatus_convertToEntityAttribute() {
        assertAll(
                () -> assertThat(paymentStatusConverter.convertToEntityAttribute("APPROVED")).isEqualTo(PaymentStatus.APPROVED),
                () -> assertThat(paymentStatusConverter.convertToEntityAttribute(null)).isNull(),
                () -> assertThat(paymentStatusConverter.convertToEntityAttribute("")).isNull()
        );
    }

    @Test
    @DisplayName("PaymentCancelType를 DB 컬럼 값으로 변환한다.")
    void paymentCancelType_convertToDatabaseColumn() {
        assertAll(
                () -> assertThat(paymentCancelTypeConverter.convertToDatabaseColumn(PaymentCancelType.CANCEL)).isEqualTo("CANCEL"),
                () -> assertThat(paymentCancelTypeConverter.convertToDatabaseColumn(null)).isNull()
        );
    }

    @Test
    @DisplayName("DB 컬럼 값을 PaymentCancelType로 변환한다.")
    void paymentCancelType_convertToEntityAttribute() {
        assertAll(
                () -> assertThat(paymentCancelTypeConverter.convertToEntityAttribute("CANCEL")).isEqualTo(PaymentCancelType.CANCEL),
                () -> assertThat(paymentCancelTypeConverter.convertToEntityAttribute(null)).isNull(),
                () -> assertThat(paymentCancelTypeConverter.convertToEntityAttribute("")).isNull()
        );
    }
}
