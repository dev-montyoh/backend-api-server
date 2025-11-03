package io.github.monty.api.user.infrastructure.repository.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.constants.StaticValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock
public class WireMockTest {
    @Autowired
    protected ObjectMapper mapper;

    protected void setupSuccessResponse(String requestHttpMethod, String requestUrl, Object responseBody) {
        try {
            WireMock.stubFor(
                    WireMock.request(requestHttpMethod, WireMock.urlMatching(requestUrl))
                            .willReturn(WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader(StaticValues.HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(mapper.writeValueAsString(responseBody)))
            );
        } catch (JsonProcessingException exception) {
            log.error("MockServer Body 파싱 도중 에러 발생", exception);
        }
    }

    protected void setupFailResponse(String requestHttpMethod, String requestUrl, ErrorCode errorCode) {
        try {
            WireMock.stubFor(
                    WireMock.request(requestHttpMethod, WireMock.urlMatching(requestUrl))
                            .willReturn(WireMock.aResponse()
                                    .withStatus(errorCode.getHttpStatus().value())
                                    .withHeader(StaticValues.HEADER_ERROR_CODE, errorCode.getCode())
                                    .withHeader(StaticValues.HEADER_ERROR_MESSAGE, errorCode.getMessage())
                                    .withHeader(StaticValues.HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .withBody(mapper.writeValueAsString(null)))
            );
        } catch (JsonProcessingException exception) {
            log.error("MockServer Body 파싱 도중 에러 발생", exception);
        }
    }
}
