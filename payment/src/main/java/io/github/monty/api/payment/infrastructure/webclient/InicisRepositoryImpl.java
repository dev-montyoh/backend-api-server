package io.github.monty.api.payment.infrastructure.webclient;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.ConvertUtils;
import io.github.monty.api.payment.domain.model.vo.*;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.infrastructure.constants.InicisApiUrl;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentApprovalResponse;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentCancelResponse;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import io.github.monty.api.payment.infrastructure.webclient.mapper.InicisPaymentApprovalMapper;
import io.github.monty.api.payment.infrastructure.webclient.mapper.InicisPaymentCancelMapper;
import io.github.monty.api.payment.infrastructure.webclient.mapper.InicisPaymentNetworkCancelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class InicisRepositoryImpl implements InicisRepository {

    private final WebClient webClient;

    private final InicisPaymentApprovalMapper inicisPaymentApprovalMapper;
    private final InicisPaymentCancelMapper inicisPaymentCancelMapper;
    private final InicisPaymentNetworkCancelMapper inicisPaymentNetworkCancelMapper;

    private static final String INICIS_RESPONSE_DATA_FORMAT = "JSON";
    private static final String INICIS_RESPONSE_RESULT_CODE_SUCCESS = "0000";
    private static final String INICIS_RESPONSE_RESULT_CODE_ALREADY_CANCELLED = "500626";

    private static final String INICIS_RESPONSE_RESULT_CODE_SUCCESS_V2 = "00";

    /**
     * 이니시스로 결제 승인 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentApprovalReqVo 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    @Override
    public InicisPaymentApprovalResVo requestApprovePayment(InicisPaymentApprovalReqVo inicisPaymentApprovalReqVo) {
        MultiValueMap<String, String> formData = ConvertUtils.convertToMultiValueMap(inicisPaymentApprovalReqVo);
        formData.add("charset", StandardCharsets.UTF_8.name());
        formData.add("format", INICIS_RESPONSE_DATA_FORMAT);

        InicisPaymentApprovalResponse inicisPaymentApprovalResponse = webClient.post()
                .uri(inicisPaymentApprovalReqVo.getAuthUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(InicisPaymentApprovalResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentApprovalResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        if (!inicisPaymentApprovalResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL, inicisPaymentApprovalResponse.getResultMsg());
        }

        return inicisPaymentApprovalMapper.mapToVo(inicisPaymentApprovalResponse);
    }

    /**
     * 이니시스로 결제 취소 요청을 한다.
     * Content-type: application/json
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentCancelReqVo 결제 취소 요청 VO
     * @return 취소 요청 결과 VO
     */
    @Override
    public InicisPaymentCancelResVo requestCancelPayment(InicisPaymentCancelReqVo inicisPaymentCancelReqVo) {
        InicisPaymentCancelResponse inicisPaymentCancelResponse = webClient.post()
                .uri(InicisApiUrl.INICIS_PAYMENT_CANCEL_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(inicisPaymentCancelReqVo)
                .retrieve()
                .bodyToMono(InicisPaymentCancelResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentCancelResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        if (inicisPaymentCancelResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_ALREADY_CANCELLED)) {
            throw new ApplicationException(ErrorCode.ERROR_ALREADY_CANCELLED, inicisPaymentCancelResponse.getResultMsg());
        }

        if (!inicisPaymentCancelResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS_V2)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL, inicisPaymentCancelResponse.getResultMsg());
        }

        return inicisPaymentCancelMapper.mapToVo(inicisPaymentCancelResponse, inicisPaymentCancelReqVo.getData().getMsg());
    }

    /**
     * 이니시스로 결제 망취소 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentNetworkCancelReqVo 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    @Override
    public InicisPaymentNetworkCancelResVo requestNetworkCancelPayment(InicisPaymentNetworkCancelReqVo inicisPaymentNetworkCancelReqVo) {
        MultiValueMap<String, String> formData = ConvertUtils.convertToMultiValueMap(inicisPaymentNetworkCancelReqVo);
        formData.add("charset", StandardCharsets.UTF_8.name());
        formData.add("format", INICIS_RESPONSE_DATA_FORMAT);

        InicisPaymentNetworkCancelResponse inicisPaymentNetworkCancelResponse = webClient.post()
                .uri(inicisPaymentNetworkCancelReqVo.getNetworkCancelUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(InicisPaymentNetworkCancelResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentNetworkCancelResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        if (!inicisPaymentNetworkCancelResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL, inicisPaymentNetworkCancelResponse.getResultMsg());
        }

        return inicisPaymentNetworkCancelMapper.mapToVo(inicisPaymentNetworkCancelResponse);
    }
}
