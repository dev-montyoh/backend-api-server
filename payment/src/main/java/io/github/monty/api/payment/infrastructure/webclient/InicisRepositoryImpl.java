package io.github.monty.api.payment.infrastructure.webclient;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.infrastructure.webclient.dto.InicisPaymentApprovalResponse;
import io.github.monty.api.payment.infrastructure.webclient.mapper.InicisPaymentApprovalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
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

    private static final String INICIS_RESPONSE_DATA_FORMAT = "JSON";
    private static final String INICIS_RESPONSE_RESULT_CODE_SUCCESS = "0000";

    /**
     * 이니시스로 결제 승인 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentApprovalRequestVO 결제 승인 요청 VO
     */
    @Override
    public InicisPaymentApprovalResultVO requestApprovePayment(InicisPaymentApprovalRequestVO inicisPaymentApprovalRequestVO) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("mid", inicisPaymentApprovalRequestVO.getMid());
        formData.add("authToken", inicisPaymentApprovalRequestVO.getAuthToken());
        formData.add("timestamp", String.valueOf(inicisPaymentApprovalRequestVO.getTimestamp()));
        formData.add("signature", inicisPaymentApprovalRequestVO.getSignature());
        formData.add("verification", inicisPaymentApprovalRequestVO.getVerification());
        formData.add("charset", StandardCharsets.UTF_8.name());
        formData.add("format", INICIS_RESPONSE_DATA_FORMAT);

        InicisPaymentApprovalResponse inicisPaymentApprovalResponse = webClient.post()
                .uri(inicisPaymentApprovalRequestVO.getAuthUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(InicisPaymentApprovalResponse.class)
                .block();

        if (ObjectUtils.isEmpty(inicisPaymentApprovalResponse) || !inicisPaymentApprovalResponse.getResultCode().equals(INICIS_RESPONSE_RESULT_CODE_SUCCESS)) {
            throw new ApplicationException(ErrorCode.ERROR_PAYMENT_APPROVAL);
        }

        return inicisPaymentApprovalMapper.mapToVo(inicisPaymentApprovalResponse);
    }
}
