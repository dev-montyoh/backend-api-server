package io.github.monty.api.payment.infrastructure.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.ConvertUtils;
import io.github.monty.api.payment.domain.model.vo.*;
import io.github.monty.api.payment.domain.repository.NicepayRepository;
import io.github.monty.api.payment.infrastructure.constants.ApiUrl;
import io.github.monty.api.payment.infrastructure.webclient.dto.*;
import io.github.monty.api.payment.infrastructure.webclient.mapper.NicepayPaymentApprovalMapper;
import io.github.monty.api.payment.infrastructure.webclient.mapper.NicepayPaymentCancelMapper;
import io.github.monty.api.payment.infrastructure.webclient.mapper.NicepayPaymentNetworkCancelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicepayRepositoryImpl implements NicepayRepository {

    private final WebClient webClient;
    private final NicepayPaymentApprovalMapper nicepayPaymentApprovalMapper;
    private final NicepayPaymentNetworkCancelMapper nicepayPaymentNetworkCancelMapper;
    private final NicepayPaymentCancelMapper nicepayPaymentCancelMapper;

    private static final String NICEPAY_RESPONSE_DATA_FORMAT = "JSON";
    private static final String NICEPAY_RESPONSE_CHAR_SET = "utf-8";
    private static final String NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_CARD = "3001";
    private static final String NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_BANK = "4000";
    private static final String NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_VBANK = "4100";
    private static final String NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_PHONE = "A000";
    private static final String NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_CASH_RECEIPT = "7001";
    private static final String NICEPAY_NETWORK_CANCEL_RESPONSE_RESULT_CODE_SUCCESS = "2001";
    private static final String NICEPAY_CANCEL_RESPONSE_RESULT_CODE_SUCCESS = "2001";

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 나이스페이로 결제 승인 요청을 한다.
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentApprovalReqVo 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    @Override
    public NicepayPaymentApprovalResVo requestApprovePayment(NicepayPaymentApprovalReqVo nicepayPaymentApprovalReqVo) {
        NicepayPaymentApprovalRequest nicepayPaymentApprovalRequest = nicepayPaymentApprovalMapper.mapToDto(nicepayPaymentApprovalReqVo, NICEPAY_RESPONSE_DATA_FORMAT, NICEPAY_RESPONSE_CHAR_SET);
        MultiValueMap<String, String> formData = ConvertUtils.convertToMultiValueMap(nicepayPaymentApprovalRequest);

        String responseData = webClient.post()
                .uri(nicepayPaymentApprovalReqVo.getNextApprovalUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        NicepayPaymentApprovalResponse nicepayPaymentApprovalResponse = this.objectFromJson(responseData, NicepayPaymentApprovalResponse.class);

        if (ObjectUtils.isEmpty(nicepayPaymentApprovalResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        if (!List.of(NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_CARD, NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_BANK, NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_VBANK,
                NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_PHONE, NICEPAY_RESPONSE_RESULT_CODE_SUCCESS_CASH_RECEIPT).contains(nicepayPaymentApprovalResponse.getResultCode())) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL, nicepayPaymentApprovalResponse.getResultMsg());
        }

        return nicepayPaymentApprovalMapper.mapToVo(nicepayPaymentApprovalResponse);
    }

    /**
     * 나이스페이로 결제 망취소 요청을 한다.
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentNetworkCancelReqVo 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    @Override
    public NicepayPaymentNetworkCancelResVo requestNetworkCancelPayment(NicepayPaymentNetworkCancelReqVo nicepayPaymentNetworkCancelReqVo) {
        NicepayPaymentNetworkCancelRequest nicepayPaymentNetworkCancelRequest = nicepayPaymentNetworkCancelMapper.mapToDto(nicepayPaymentNetworkCancelReqVo, NICEPAY_RESPONSE_DATA_FORMAT, NICEPAY_RESPONSE_CHAR_SET, StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR);
        MultiValueMap<String, String> formData = ConvertUtils.convertToMultiValueMap(nicepayPaymentNetworkCancelRequest);

        String responseData = webClient.post()
                .uri(nicepayPaymentNetworkCancelReqVo.getNetworkCancelUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        NicepayPaymentNetworkCancelResponse nicepayPaymentNetworkCancelResponse = this.objectFromJson(responseData, NicepayPaymentNetworkCancelResponse.class);

        if (ObjectUtils.isEmpty(nicepayPaymentNetworkCancelResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL);
        }

        if (!nicepayPaymentNetworkCancelResponse.getResultCode().equals(NICEPAY_NETWORK_CANCEL_RESPONSE_RESULT_CODE_SUCCESS)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL, nicepayPaymentNetworkCancelResponse.getResultMsg());
        }

        return nicepayPaymentNetworkCancelMapper.mapToVo(nicepayPaymentNetworkCancelResponse);
    }

    /**
     * 나이스페이로 결제 취소 요청을 한다. (전체)
     * Target <a href="https://pg-api.nicepay.co.kr/webapi/cancel_process.jsp">Nicepay Cancel</a>
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentCancelReqVo 결제 취소 요청 VO
     * @return 취소 요청 결과 VO
     */
    @Override
    public NicepayPaymentCancelResVo requestCancelPayment(NicepayPaymentCancelReqVo nicepayPaymentCancelReqVo) {
        NicepayPaymentCancelRequest nicepayPaymentCancelRequest = nicepayPaymentCancelMapper.mapToDto(nicepayPaymentCancelReqVo, NICEPAY_RESPONSE_DATA_FORMAT, NICEPAY_RESPONSE_CHAR_SET);
        MultiValueMap<String, String> formData = ConvertUtils.convertToMultiValueMap(nicepayPaymentCancelRequest);

        String responseData = webClient.post()
                .uri(ApiUrl.NICEPAY_PAYMENT_CANCEL_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        NicepayPaymentCancelResponse nicepayPaymentCancelResponse = this.objectFromJson(responseData, NicepayPaymentCancelResponse.class);

        if (ObjectUtils.isEmpty(nicepayPaymentCancelResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL);
        }

        if (!nicepayPaymentCancelResponse.getResultCode().equals(NICEPAY_CANCEL_RESPONSE_RESULT_CODE_SUCCESS)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_CANCEL, nicepayPaymentCancelResponse.getResultMsg());
        }

        return nicepayPaymentCancelMapper.mapToVo(nicepayPaymentCancelResponse, nicepayPaymentCancelReqVo.getCancelReason());
    }

    /**
     * JSON TO OBJECT
     *
     * @param jsonString JSON 문자열
     * @param clazz      변환 대상 클래스
     * @return 변환 결과 객체
     */
    private <T> T objectFromJson(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE)
                    .readValue(jsonString, clazz);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error(jsonProcessingException.getMessage(), jsonProcessingException);
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
