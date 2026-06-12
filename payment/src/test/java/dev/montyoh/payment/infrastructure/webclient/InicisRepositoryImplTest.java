package dev.montyoh.payment.infrastructure.webclient;

import dev.montyoh.payment.common.constants.ErrorCode;
import dev.montyoh.payment.common.exception.ApplicationException;
import dev.montyoh.payment.domain.model.vo.*;
import dev.montyoh.payment.infrastructure.webclient.dto.InicisPaymentApprovalResponse;
import dev.montyoh.payment.infrastructure.webclient.dto.InicisPaymentCancelResponse;
import dev.montyoh.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import dev.montyoh.payment.infrastructure.webclient.mapper.InicisPaymentApprovalMapper;
import dev.montyoh.payment.infrastructure.webclient.mapper.InicisPaymentCancelMapper;
import dev.montyoh.payment.infrastructure.webclient.mapper.InicisPaymentNetworkCancelMapper;
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
class InicisRepositoryImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private InicisPaymentApprovalMapper inicisPaymentApprovalMapper;
    @Mock
    private InicisPaymentCancelMapper inicisPaymentCancelMapper;
    @Mock
    private InicisPaymentNetworkCancelMapper inicisPaymentNetworkCancelMapper;

    private InicisRepositoryImpl inicisRepository;

    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        inicisRepository = new InicisRepositoryImpl(webClient, inicisPaymentApprovalMapper, inicisPaymentCancelMapper, inicisPaymentNetworkCancelMapper);

        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        given(webClient.post()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.uri(anyString())).willReturn(requestBodySpec);
        given(requestBodySpec.header(anyString(), anyString())).willReturn(requestBodySpec);
        given(requestBodySpec.body(any())).willReturn(requestHeadersSpec);
        given(requestBodySpec.bodyValue(any())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
    }

    @Test
    @DisplayName("requestApprovePayment - 성공 시 승인 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_success() {
        InicisPaymentApprovalReqVo reqVo = InicisPaymentApprovalReqVo.builder()
                .mid("testMid").authToken("testToken").timestamp(1234567L)
                .signature("testSig").verification("testVerify").authUrl("http://auth.url")
                .build();
        InicisPaymentApprovalResponse response = InicisPaymentApprovalResponse.builder()
                .resultCode("0000").resultMsg("정상 처리").tid("testTid").build();
        InicisPaymentApprovalResVo expectedVo = InicisPaymentApprovalResVo.builder()
                .resultMessage("정상 처리").tid("testTid").build();

        given(responseSpec.bodyToMono(InicisPaymentApprovalResponse.class)).willReturn(Mono.just(response));
        given(inicisPaymentApprovalMapper.mapToVo(response)).willReturn(expectedVo);

        InicisPaymentApprovalResVo result = inicisRepository.requestApprovePayment(reqVo);

        assertThat(result.getTid()).isEqualTo("testTid");
    }

    @Test
    @DisplayName("requestApprovePayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_emptyResponse() {
        InicisPaymentApprovalReqVo reqVo = InicisPaymentApprovalReqVo.builder()
                .mid("testMid").authToken("testToken").timestamp(1234567L)
                .signature("testSig").verification("testVerify").authUrl("http://auth.url")
                .build();
        given(responseSpec.bodyToMono(InicisPaymentApprovalResponse.class)).willReturn(Mono.empty());

        assertThatThrownBy(() -> inicisRepository.requestApprovePayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()));
    }

    @Test
    @DisplayName("requestApprovePayment - 실패 코드이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestApprovePayment_fail() {
        InicisPaymentApprovalReqVo reqVo = InicisPaymentApprovalReqVo.builder()
                .mid("testMid").authToken("testToken").timestamp(1234567L)
                .signature("testSig").verification("testVerify").authUrl("http://auth.url")
                .build();
        InicisPaymentApprovalResponse response = InicisPaymentApprovalResponse.builder()
                .resultCode("9999").resultMsg("결제 실패").build();
        given(responseSpec.bodyToMono(InicisPaymentApprovalResponse.class)).willReturn(Mono.just(response));

        assertThatThrownBy(() -> inicisRepository.requestApprovePayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()));
    }

    @Test
    @DisplayName("requestCancelPayment - 성공 시 취소 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_success() {
        InicisPaymentCancelReqVo reqVo = new InicisPaymentCancelReqVo(
                "testMid", "refund", "20260612120000", "127.0.0.1", "testHash",
                new InicisPaymentCancelReqVo.Data("testTid", "취소 사유")
        );
        InicisPaymentCancelResponse response = InicisPaymentCancelResponse.builder()
                .resultCode("00").resultMsg("취소 성공").cancelDate("20260612").cancelTime("120000").build();
        InicisPaymentCancelResVo expectedVo = InicisPaymentCancelResVo.builder()
                .resultMessage("취소 성공").build();

        given(responseSpec.bodyToMono(InicisPaymentCancelResponse.class)).willReturn(Mono.just(response));
        given(inicisPaymentCancelMapper.mapToVo(response, "취소 사유")).willReturn(expectedVo);

        InicisPaymentCancelResVo result = inicisRepository.requestCancelPayment(reqVo);

        assertThat(result.getResultMessage()).isEqualTo("취소 성공");
    }

    @Test
    @DisplayName("requestCancelPayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_emptyResponse() {
        InicisPaymentCancelReqVo reqVo = new InicisPaymentCancelReqVo(
                "testMid", "refund", "20260612120000", "127.0.0.1", "testHash",
                new InicisPaymentCancelReqVo.Data("testTid", "취소 사유")
        );
        given(responseSpec.bodyToMono(InicisPaymentCancelResponse.class)).willReturn(Mono.empty());

        assertThatThrownBy(() -> inicisRepository.requestCancelPayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_APPROVAL.getCode()));
    }

    @Test
    @DisplayName("requestCancelPayment - 이미 취소된 경우 ERROR_ALREADY_CANCELLED 를 던진다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_alreadyCancelled() {
        InicisPaymentCancelReqVo reqVo = new InicisPaymentCancelReqVo(
                "testMid", "refund", "20260612120000", "127.0.0.1", "testHash",
                new InicisPaymentCancelReqVo.Data("testTid", "취소 사유")
        );
        InicisPaymentCancelResponse response = InicisPaymentCancelResponse.builder()
                .resultCode("500626").resultMsg("이미 취소됨").build();
        given(responseSpec.bodyToMono(InicisPaymentCancelResponse.class)).willReturn(Mono.just(response));

        assertThatThrownBy(() -> inicisRepository.requestCancelPayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_ALREADY_CANCELLED.getCode()));
    }

    @Test
    @DisplayName("requestCancelPayment - 실패 코드이면 ERROR_PAYMENT_CANCEL 을 던진다.")
    @SuppressWarnings("unchecked")
    void requestCancelPayment_fail() {
        InicisPaymentCancelReqVo reqVo = new InicisPaymentCancelReqVo(
                "testMid", "refund", "20260612120000", "127.0.0.1", "testHash",
                new InicisPaymentCancelReqVo.Data("testTid", "취소 사유")
        );
        InicisPaymentCancelResponse response = InicisPaymentCancelResponse.builder()
                .resultCode("9999").resultMsg("취소 실패").build();
        given(responseSpec.bodyToMono(InicisPaymentCancelResponse.class)).willReturn(Mono.just(response));

        assertThatThrownBy(() -> inicisRepository.requestCancelPayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 성공 시 망취소 결과 VO를 반환한다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_success() {
        InicisPaymentNetworkCancelReqVo reqVo = new InicisPaymentNetworkCancelReqVo(
                "testMid", "testAuthToken", 1234567L, "testSig", "testVerify", "http://network-cancel.url"
        );
        InicisPaymentNetworkCancelResponse response = InicisPaymentNetworkCancelResponse.builder()
                .resultCode("0000").resultMsg("망취소 성공").tid("testTid").build();
        InicisPaymentNetworkCancelResVo expectedVo = InicisPaymentNetworkCancelResVo.builder()
                .resultMessage("망취소 성공").tid("testTid").build();

        given(responseSpec.bodyToMono(InicisPaymentNetworkCancelResponse.class)).willReturn(Mono.just(response));
        given(inicisPaymentNetworkCancelMapper.mapToVo(response)).willReturn(expectedVo);

        InicisPaymentNetworkCancelResVo result = inicisRepository.requestNetworkCancelPayment(reqVo);

        assertThat(result.getTid()).isEqualTo("testTid");
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 빈 응답이면 ApplicationException을 던진다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_emptyResponse() {
        InicisPaymentNetworkCancelReqVo reqVo = new InicisPaymentNetworkCancelReqVo(
                "testMid", "testAuthToken", 1234567L, "testSig", "testVerify", "http://network-cancel.url"
        );
        given(responseSpec.bodyToMono(InicisPaymentNetworkCancelResponse.class)).willReturn(Mono.empty());

        assertThatThrownBy(() -> inicisRepository.requestNetworkCancelPayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }

    @Test
    @DisplayName("requestNetworkCancelPayment - 실패 코드이면 ERROR_PAYMENT_CANCEL 을 던진다.")
    @SuppressWarnings("unchecked")
    void requestNetworkCancelPayment_fail() {
        InicisPaymentNetworkCancelReqVo reqVo = new InicisPaymentNetworkCancelReqVo(
                "testMid", "testAuthToken", 1234567L, "testSig", "testVerify", "http://network-cancel.url"
        );
        InicisPaymentNetworkCancelResponse response = InicisPaymentNetworkCancelResponse.builder()
                .resultCode("9999").resultMsg("망취소 실패").build();
        given(responseSpec.bodyToMono(InicisPaymentNetworkCancelResponse.class)).willReturn(Mono.just(response));

        assertThatThrownBy(() -> inicisRepository.requestNetworkCancelPayment(reqVo))
                .isInstanceOf(ApplicationException.class)
                .satisfies(e -> assertThat(((ApplicationException) e).getErrorCode())
                        .isEqualTo(ErrorCode.ERROR_PAYMENT_CANCEL.getCode()));
    }
}
