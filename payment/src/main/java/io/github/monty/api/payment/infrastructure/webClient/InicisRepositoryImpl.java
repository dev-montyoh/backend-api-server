package io.github.monty.api.payment.infrastructure.webClient;

import io.github.monty.api.payment.domain.model.vo.InicisPaymentApproveRequestVO;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.infrastructure.webClient.dto.InicisPaymentApproveRequest;
import io.github.monty.api.payment.infrastructure.webClient.mapper.InicisWebClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class InicisRepositoryImpl implements InicisRepository {

    private final InicisWebClientMapper inicisWebClientMapper;
    private final WebClient webClient;

    private static final String INICIS_RESPONSE_DATA_FORMAT = "JSON";

    @Override
    public void requestApprovePayment(InicisPaymentApproveRequestVO inicisPaymentApproveRequestVO) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("mid", inicisPaymentApproveRequestVO.getMid());
        formData.add("authToken", inicisPaymentApproveRequestVO.getAuthToken());
        formData.add("timestamp", String.valueOf(inicisPaymentApproveRequestVO.getTimestamp()));
        formData.add("signature", inicisPaymentApproveRequestVO.getSignature());
        formData.add("verification", inicisPaymentApproveRequestVO.getVerification());
        formData.add("charset", StandardCharsets.UTF_8.name());
        formData.add("format", INICIS_RESPONSE_DATA_FORMAT);


        String block = webClient.post()
                .uri(inicisPaymentApproveRequestVO.getAuthUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(block);
    }
}
