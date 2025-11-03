package io.github.monty.api.payment.common.exception.handler;

import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.common.exception.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * ApplicationException.class 에 대한 예외 처리를 한다.
     *
     * @param applicationException 발생한 예외
     * @return 예외 결과 값을 헤더에 담은 ResponseEntity
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationExceptionHandle(ApplicationException applicationException) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(StaticValues.HEADER_ERROR_CODE, applicationException.getErrorCode());
        headers.add(StaticValues.HEADER_ERROR_MESSAGE, URLEncoder.encode(applicationException.getMessage(), StandardCharsets.UTF_8));
        return new ResponseEntity<>(null, headers, applicationException.getHttpStatus());
    }
}
