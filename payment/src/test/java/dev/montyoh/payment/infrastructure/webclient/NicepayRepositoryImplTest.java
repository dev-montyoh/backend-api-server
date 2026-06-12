package dev.montyoh.payment.infrastructure.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.vo.*;
import dev.montyoh.payment.infrastructure.webclient.dto.*;
import dev.montyoh.payment.infrastructure.webclient.mapper.NicepayPaymentApprovalMapper;
import dev.montyoh.payment.infrastructure.webclient.mapper.NicepayPaymentCancelMapper;
import dev.montyoh.payment.infrastructure.webclient.mapper.NicepayPaymentNetworkCancelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NicepayRepositoryImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private NicepayPaymentApprovalMapper nicepayPaymentApprovalMapper;
    @Mock
    private NicepayPaymentNetworkCancelMapper nicepayPaymentNetworkCancelMapper;
    @Mock
    private NicepayPaymentCancelMapper nicepayPaymentCancelMapper;

    private NicepayRepositoryImpl nicepayRepository;

    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        nicepayRepository = new NicepayRepositoryImpl(webClient, nicepayPaymentApprovalMapper, nicepayPaymentNetworkCancelMapper, nicepayPaymentCancelMapper);

        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        given(webClient.post()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.uri(anyString())).willReturn(requestBodySpec);
        given(requestBodySpec.header(anyString(), anyString())).willReturn(requestBodySpec);
        given(requestBodySpec.body(any())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.accept(any())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private NicepayPaymentApprovalReqVo buildApprovalReqVo() {
        return NicepayPaymentApprovalReqVo.builder()
                .transactionId("testTxId").authToken("testAuthToken").mid("testMid")
                .amount("10000").ediDate("20260612120000").signData("testSignData")
                .mallReserved("").nextApprovalUrl("http://next-approval.url")
                .build();
    }

    private NicepayPaymentNetworkCancelReqVo buildNetworkCancelReqVo() {
        return new NicepayPaymentNetworkCancelReqVo(
                "testTxId", "testAuthToken", "testMid", 10000L, "20260612120000",
                "testSignData", "", "http://network-cancel.url", "testOrderNo"
        );
    }

    private NicepayPaymentCancelReqVo buildCancelReqVo() {
        return new NicepayPaymentCancelReqVo(
                "testTxId", "testMid", "testOrderNo", 10000L, "취소 사유",
                "20260612120000", "testSignData", ""
        );
    }

    @Test
    @DisplayName("requestApprovePayment - 성공 시 승인 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_success() {
        NicepayPaymentApprovalRequest request = NicepayPaymentApprovalRequest.builder().build();
        NicepayPaymentApprovalResponse response = NicepayPaymentApprovalResponse.builder()
                .resultCode("3001").resultMsg("성공").tid("testTid").authDate("260612120000")
                .build();
        NicepayPaymentApprovalResVo expectedVo = NicepayPaymentApprovalResVo.builder()
                .resultMessage("성공").tid("testTid").build();

        given(nicepayPaymentApprovalMapper.mapToDto(any(), anyString(), anyString())).willReturn(request);
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));
        given(nicepayPaymentApprovalMapper.mapToVo(any(NicepayPaymentApprovalResponse.class))).willReturn(expectedVo);

        NicepayPaymentApprovalResVo result = nicepayRepository.requestApprovePayment(buildApprovalReqVo());

        assertThat(result.getTid()).isEqualTo("testTid");
    }

    @Test
    @DisplayName("requestApprovePayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_emptyResponse() {
        given(nicepayPaymentApprovalMapper.mapToDto(any(), anyString(), anyString()))
                .willReturn(NicepayPaymentApprovalRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just("null"));

        assertThatThrownBy(() -> nicepayRepository.requestApprovePayment(buildApprovalReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()));
    }

    @Test
    @DisplayName("requestApprovePayment - 실패 코드이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_fail() {
        NicepayPaymentApprovalResponse response = NicepayPaymentApprovalResponse.builder()
                .resultCode("9999").resultMsg("실패").build();
        given(nicepayPaymentApprovalMapper.mapToDto(any(), anyString(), anyString()))
                .willReturn(NicepayPaymentApprovalRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));

        assertThatThrownBy(() -> nicepayRepository.requestApprovePayment(buildApprovalReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()));
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 성공 시 망취소 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_success() {
        NicepayPaymentNetworkCancelRequest request = NicepayPaymentNetworkCancelRequest.builder().build();
        NicepayPaymentNetworkCancelResponse response = NicepayPaymentNetworkCancelResponse.builder()
                .resultCode("2001").resultMsg("망취소 성공").tid("testTid").build();
        NicepayPaymentNetworkCancelResVo expectedVo = NicepayPaymentNetworkCancelResVo.builder()
                .resultMessage("망취소 성공").tid("testTid").build();

        given(nicepayPaymentNetworkCancelMapper.mapToDto(any(), anyString(), anyString(), anyString())).willReturn(request);
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));
        given(nicepayPaymentNetworkCancelMapper.mapToVo(any(NicepayPaymentNetworkCancelResponse.class))).willReturn(expectedVo);

        NicepayPaymentNetworkCancelResVo result = nicepayRepository.requestNetworkCancelPayment(buildNetworkCancelReqVo());

        assertThat(result.getTid()).isEqualTo("testTid");
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_emptyResponse() {
        given(nicepayPaymentNetworkCancelMapper.mapToDto(any(), anyString(), anyString(), anyString()))
                .willReturn(NicepayPaymentNetworkCancelRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just("null"));

        assertThatThrownBy(() -> nicepayRepository.requestNetworkCancelPayment(buildNetworkCancelReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 실패 코드이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_fail() {
        NicepayPaymentNetworkCancelResponse response = NicepayPaymentNetworkCancelResponse.builder()
                .resultCode("9999").resultMsg("망취소 실패").build();
        given(nicepayPaymentNetworkCancelMapper.mapToDto(any(), anyString(), anyString(), anyString()))
                .willReturn(NicepayPaymentNetworkCancelRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));

        assertThatThrownBy(() -> nicepayRepository.requestNetworkCancelPayment(buildNetworkCancelReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("requestCancelPayment - 성공 시 취소 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_success() {
        NicepayPaymentCancelRequest request = NicepayPaymentCancelRequest.builder().build();
        NicepayPaymentCancelResponse response = NicepayPaymentCancelResponse.builder()
                .resultCode("2001").resultMsg("취소 성공").tid("testTid").build();
        NicepayPaymentCancelResVo expectedVo = NicepayPaymentCancelResVo.builder()
                .resultMessage("취소 성공").build();

        given(nicepayPaymentCancelMapper.mapToDto(any(), anyString(), anyString())).willReturn(request);
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));
        given(nicepayPaymentCancelMapper.mapToVo(any(NicepayPaymentCancelResponse.class), anyString())).willReturn(expectedVo);

        NicepayPaymentCancelResVo result = nicepayRepository.requestCancelPayment(buildCancelReqVo());

        assertThat(result.getResultMessage()).isEqualTo("취소 성공");
    }

    @Test
    @DisplayName("requestCancelPayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_emptyResponse() {
        given(nicepayPaymentCancelMapper.mapToDto(any(), anyString(), anyString()))
                .willReturn(NicepayPaymentCancelRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just("null"));

        assertThatThrownBy(() -> nicepayRepository.requestCancelPayment(buildCancelReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("requestCancelPayment - 실패 코드이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_fail() {
        NicepayPaymentCancelResponse response = NicepayPaymentCancelResponse.builder()
                .resultCode("9999").resultMsg("취소 실패").build();
        given(nicepayPaymentCancelMapper.mapToDto(any(), anyString(), anyString()))
                .willReturn(NicepayPaymentCancelRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just(toJson(response)));

        assertThatThrownBy(() -> nicepayRepository.requestCancelPayment(buildCancelReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("objectFromJson - JSON 파싱 실패 시 INTERNAL_SERVER_ERROR 를 던진다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_invalidJson() {
        given(nicepayPaymentApprovalMapper.mapToDto(any(), anyString(), anyString()))
                .willReturn(NicepayPaymentApprovalRequest.builder().build());
        given(responseSpec.bodyToMono(String.class)).willReturn(Mono.just("invalid-json!!!"));

        assertThatThrownBy(() -> nicepayRepository.requestApprovePayment(buildApprovalReqVo()))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
    }
}
