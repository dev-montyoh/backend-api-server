package dev.montyoh.payment.common.exception.handler;

import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.constants.StaticValues;
import dev.montyoh.payment.common.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ApplicationExceptionHandlerTest {

    private final ApplicationExceptionHandler handler = new ApplicationExceptionHandler();

    @Test
    @DisplayName("ApplicationException 이 발생하면 에러 코드와 메시지를 헤더에 담아 반환한다.")
    void applicationExceptionHandle_returnsResponseWithHeaders() {
        ApplicationException ex = new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);

        ResponseEntity<?> response = handler.applicationExceptionHandle(ex);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getHttpStatus()),
                () -> assertThat(response.getHeaders().getFirst(StaticValues.HEADER_ERROR_CODE))
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()),
                () -> assertThat(response.getHeaders().getFirst(StaticValues.HEADER_ERROR_MESSAGE))
                        .isNotBlank(),
                () -> assertThat(response.getBody()).isNull()
        );
    }

    @Test
    @DisplayName("다양한 ErrorCode 에 대해 해당 HTTP 상태와 에러 코드를 반환한다.")
    void applicationExceptionHandle_withDifferentErrorCodes() {
        ApplicationException notFoundEx = new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA);

        ResponseEntity<?> response = handler.applicationExceptionHandle(notFoundEx);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(ErrorCode.NOT_EXIST_PAYMENT_DATA.getHttpStatus()),
                () -> assertThat(response.getHeaders().getFirst(StaticValues.HEADER_ERROR_CODE))
                        .isEqualTo(ErrorCode.NOT_EXIST_PAYMENT_DATA.getCode())
        );
    }
}
