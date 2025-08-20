package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //  공통 에러
    INTERNAL_SERVER_ERROR("0001", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_SERVER_ERROR("0002", "외부 서비스 호출 도중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    //  결제 서비스 오류
    NOT_EXIST_PAYMENT_SERVICE("0101", "잘못된 결제 서비스 요청 입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_PAYMENT_DATA("0102", "결제 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    //  암호화 오류
    NOT_EXIST_ENCRYPT_ALGORITHM("0004", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)

    ;

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

    private static final Map<String, ErrorCode> ERROR_CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(ErrorCode::getCode, errorCode -> errorCode))
    );

    public static ErrorCode findByCode(final String code) {
        return ERROR_CODE_MAP.getOrDefault(code, INTERNAL_SERVER_ERROR);
    }
}
