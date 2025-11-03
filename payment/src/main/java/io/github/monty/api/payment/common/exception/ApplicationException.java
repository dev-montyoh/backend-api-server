package io.github.monty.api.payment.common.exception;

import io.github.monty.api.payment.common.constants.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ApplicationException extends RuntimeException {
    private final String errorCode;

    private final String errorMessage;

    private final String message;

    private final HttpStatus httpStatus;

    /**
     * 사전 정의되어 있는 에러 코드를 사용하여 필드값 지정
     *
     * @param errorCode 에러 코드
     */
    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
        errorLogging(this.errorCode, this.message);
        this.errorMessage = StringUtils.EMPTY;
    }

    /**
     * 사전 정의되어 있는 에러 코드를 사용하여 필드값 지정
     * 별도 로깅 에러 메시지 추가
     *
     * @param errorCode 에러 코드
     */
    public ApplicationException(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
        errorLogging(this.errorCode, this.message);
        this.errorMessage = errorMessage;
    }

    /**
     * 에러 로깅 메소드
     *
     * @param errorCode 에러 코드
     * @param message   에러 메시지
     */
    private void errorLogging(String errorCode, String message) {
        log.error("[" + errorCode + "]: " + message, this);
    }
}
