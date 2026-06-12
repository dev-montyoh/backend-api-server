package dev.montyoh.payment.common.configuration;

import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.exception.ApplicationException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FeignConfigurationTest {

    private final FeignConfig feignConfig = new FeignConfig();
    private final FeignErrorDecoder feignErrorDecoder = new FeignErrorDecoder();
    private final FeignRequestInterceptor feignRequestInterceptor = new FeignRequestInterceptor();
    private final WebClientConfig webClientConfig = new WebClientConfig();

    private Response buildFeignResponse(Map<String, Collection<String>> headers) {
        return Response.builder()
                .status(500)
                .reason("Internal Server Error")
                .request(Request.create(
                        Request.HttpMethod.GET,
                        "http://localhost",
                        Collections.emptyMap(),
                        null,
                        StandardCharsets.UTF_8,
                        new RequestTemplate()
                ))
                .headers(headers)
                .build();
    }

    @Test
    @DisplayName("FeignConfig.errorDecoder() 는 FeignErrorDecoder 를 반환한다.")
    void feignConfig_errorDecoder() {
        ErrorDecoder decoder = feignConfig.errorDecoder();
        assertThat(decoder).isInstanceOf(FeignErrorDecoder.class);
    }

    @Test
    @DisplayName("FeignErrorDecoder - ErrorCode 헤더가 있을 때 해당 ErrorCode 로 ApplicationException 을 반환한다.")
    void feignErrorDecoder_withErrorCodeHeader() {
        Response response = buildFeignResponse(Map.of("code", List.of("0103")));

        Exception exception = feignErrorDecoder.decode("testMethod", response);

        assertThat(exception).isInstanceOf(ApplicationException.class);
        ApplicationException appEx = (ApplicationException) exception;
        assertThat(appEx.getErrorCode()).isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode());
    }

    @Test
    @DisplayName("FeignErrorDecoder - ErrorCode 헤더가 없을 때 EXTERNAL_SERVER_ERROR 로 ApplicationException 을 반환한다.")
    void feignErrorDecoder_withoutErrorCodeHeader() {
        Response response = buildFeignResponse(Collections.emptyMap());

        Exception exception = feignErrorDecoder.decode("testMethod", response);

        assertThat(exception).isInstanceOf(ApplicationException.class);
        ApplicationException appEx = (ApplicationException) exception;
        assertThat(appEx.getErrorCode()).isEqualTo(ErrorCode.EXTERNAL_SERVER_ERROR.getCode());
    }

    @Test
    @DisplayName("FeignErrorDecoder - ErrorCode 헤더가 null 일 때 EXTERNAL_SERVER_ERROR 로 ApplicationException 을 반환한다.")
    void feignErrorDecoder_withNullHeader() {
        Response response = buildFeignResponse(Map.of("other-header", List.of("value")));

        Exception exception = feignErrorDecoder.decode("testMethod", response);

        assertThat(exception).isInstanceOf(ApplicationException.class);
        ApplicationException appEx = (ApplicationException) exception;
        assertThat(appEx.getErrorCode()).isEqualTo(ErrorCode.EXTERNAL_SERVER_ERROR.getCode());
    }

    @Test
    @DisplayName("FeignRequestInterceptor.apply() 는 Content-Type 헤더를 설정한다.")
    void feignRequestInterceptor_apply() {
        RequestTemplate template = new RequestTemplate();

        feignRequestInterceptor.apply(template);

        assertAll(
                () -> assertThat(template.headers()).containsKey("Content-Type"),
                () -> assertThat(template.headers().get("Content-Type")).contains("application/json")
        );
    }

    @Test
    @DisplayName("WebClientConfig.webClient() 는 WebClient 를 반환한다.")
    void webClientConfig_webClient() {
        WebClient webClient = webClientConfig.webClient(WebClient.builder());
        assertThat(webClient).isNotNull();
    }
}
