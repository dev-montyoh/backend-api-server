package io.github.monty.api.payment.infrastructure.webclient;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.ConvertUtils;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResultVO;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentApprovalResponse;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentNetworkCancelResponse;
import io.github.monty.api.payment.infrastructure.webclient.mapper.InicisPaymentApprovalMapper;
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
    private final InicisPaymentNetworkCancelMapper inicisPaymentNetworkCancelMapper;

    private static final String INICIS_RESPONSE_DATA_FORMAT = "JSON";
    private static final String INICIS_RESPONSE_RESULT_CODE_SUCCESS = "0000";

    /**
     * 이니시스로 결제 승인 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentApprovalRequestVO 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    @Override
    public InicisPaymentApprovalResultVO requestApprovePayment(InicisPaymentApprovalRequestVO inicisPaymentApprovalRequestVO) {
        MultiValueMap<String, String> formData = this.convertToMultiValueMap(inicisPaymentApprovalRequestVO);

        InicisPaymentApprovalResponse inicisPaymentApprovalResponse = webClient.post()
                .uri(inicisPaymentApprovalRequestVO.getAuthUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(InicisPaymentApprovalResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentApprovalResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        boolean isApproved = inicisPaymentApprovalResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS);
        return inicisPaymentApprovalMapper.mapToVo(inicisPaymentApprovalResponse, isApproved);
    }

    /**
     * 이니시스로 결제 망취소 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentNetworkCancelRequestVO 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    @Override
    public InicisPaymentNetworkCancelResultVO requestNetworkCancelPayment(InicisPaymentNetworkCancelRequestVO inicisPaymentNetworkCancelRequestVO) {
        MultiValueMap<String, String> formData = this.convertToMultiValueMap(inicisPaymentNetworkCancelRequestVO);

        InicisPaymentNetworkCancelResponse inicisPaymentNetworkCancelResponse = webClient.post()
                .uri(inicisPaymentNetworkCancelRequestVO.getNetworkCancelUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(InicisPaymentNetworkCancelResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentNetworkCancelResponse)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        boolean isNetworkCanceled =  inicisPaymentNetworkCancelResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS);
        return inicisPaymentNetworkCancelMapper.mapToVo(inicisPaymentNetworkCancelResponse, isNetworkCanceled);
    }

    /**
     * 해당 객체를 전송을 위한 MultiValueMap 객체로 변환한다.
     * 기본설정
     * - charset: UTF8
     * - format: JSON
     *
     * @param object 변환 대상 객체
     * @return 변환 결과
     */
    private MultiValueMap<String, String> convertToMultiValueMap(Object object) {
        MultiValueMap<String, String> multiValueMap = ConvertUtils.convertToMultiValueMap(object);
        multiValueMap.add("charset", StandardCharsets.UTF_8.name());
        multiValueMap.add("format", INICIS_RESPONSE_DATA_FORMAT);
        return multiValueMap;
    }
}
