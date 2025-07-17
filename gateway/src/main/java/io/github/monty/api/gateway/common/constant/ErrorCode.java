package io.github.monty.api.gateway.common.constant;

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

    //  인증,인가 에러
    NOT_FOUND_TOKEN_INFO("0101", "토큰 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED_ERROR("0102", "토큰 정보가 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_STATUS_ERROR("0103", "토큰 정보가 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND_ERROR("0104", "토큰 정보가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),

    //  회원 정보 에러
    NOT_FOUND_USER_INFO("0301", "사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("0302", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

    private static final Map<String, ErrorCode> ERROR_CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(ErrorCode::getCode, errorCode -> errorCode))
    );

    public static ErrorCode findByCode(final String code) {
        return ERROR_CODE_MAP.getOrDefault(code, EXTERNAL_SERVER_ERROR);
    }
}