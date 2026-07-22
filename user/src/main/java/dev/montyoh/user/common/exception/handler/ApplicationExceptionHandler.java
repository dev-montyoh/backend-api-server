package dev.montyoh.user.common.exception.handler;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.constants.StaticValues;
import dev.montyoh.user.common.exception.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /**
     * 요청 입력 검증(@Valid) 실패를 처리한다.
     * 헌법 원칙 VII — 검증 오류도 도메인 오류와 동일한 헤더 규약(code/message)으로 통일한다.
     *
     * @param exception 발생한 검증 예외
     * @return 오류 코드·메시지를 헤더에 담은 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidHandle(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .orElse(ErrorCode.INVALID_INPUT.getMessage());

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(StaticValues.HEADER_ERROR_CODE, ErrorCode.INVALID_INPUT.getCode());
        headers.add(StaticValues.HEADER_ERROR_MESSAGE, URLEncoder.encode(detail, StandardCharsets.UTF_8));
        return new ResponseEntity<>(null, headers, ErrorCode.INVALID_INPUT.getHttpStatus());
    }
}
