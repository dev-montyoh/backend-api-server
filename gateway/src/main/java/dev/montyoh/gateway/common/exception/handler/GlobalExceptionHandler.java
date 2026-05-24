package dev.montyoh.gateway.common.exception.handler;

import dev.montyoh.gateway.common.exception.ApplicationException;
import dev.montyoh.gateway.common.constant.ErrorCode;
import dev.montyoh.gateway.common.constant.StaticValues;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Order(-2)
@Slf4j
@Component
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    @NonNull
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        HttpHeaders serverHttpResponseHeaders = serverHttpResponse.getHeaders();

        if (ex instanceof ApplicationException applicationException) {
            serverHttpResponseHeaders.add(StaticValues.HEADER_RESPONSE_CODE, applicationException.getErrorCode());
            serverHttpResponseHeaders.add(StaticValues.HEADER_RESPONSE_MESSAGE, URLEncoder.encode(applicationException.getMessage(), StandardCharsets.UTF_8));
            serverHttpResponse.setStatusCode(applicationException.getHttpStatus());
        } else {
            serverHttpResponseHeaders.add(StaticValues.HEADER_RESPONSE_CODE, ErrorCode.INTERNAL_SERVER_ERROR.getCode());
            serverHttpResponseHeaders.add(StaticValues.HEADER_RESPONSE_MESSAGE, URLEncoder.encode(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), StandardCharsets.UTF_8));
            serverHttpResponse.setStatusCode(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
        }

        return serverHttpResponse.setComplete();
    }
}
