package io.github.monty.api.gateway.common.exception;

import io.github.monty.api.gateway.common.constant.ErrorCode;
import lombok.Getter;

/**
 * 토큰 검증 Exception
 */
@Getter
public class InvalidTokenException extends ApplicationException{
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
