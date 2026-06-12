package dev.montyoh.payment.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.payment.application.PaymentCommandService;
import dev.montyoh.payment.application.PaymentQueryService;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.NicepayPaymentCreateCommand;
import dev.montyoh.payment.domain.model.query.NicepayPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentCreateResVo;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import dev.montyoh.payment.interfaces.rest.constants.PaymentApiUrl;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentSignatureResDto;
import dev.montyoh.payment.interfaces.rest.mapper.NicepayPaymentCreateCommandMapper;
import dev.montyoh.payment.interfaces.rest.mapper.NicepayPaymentSignatureQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NicepayPaymentController.class)
class NicepayPaymentControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NicepayPaymentSignatureQueryMapper nicepayPaymentSignatureQueryMapper;
    @MockBean
    private NicepayPaymentCreateCommandMapper nicepayPaymentCreateCommandMapper;
    @MockBean
    private PaymentQueryService paymentQueryService;
    @MockBean
    private PaymentCommandService paymentCommandService;

    @Test
    @DisplayName("나이스페이 결제 인증 정보 획득에 성공한다.")
    void requestPaymentSignature_success() throws Exception {
        //  given
        NicepayPaymentSignatureQuery query = NicepayPaymentSignatureQuery.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .price("10000")
                .build();
        NicepayPaymentSignatureResVo resVo = NicepayPaymentSignatureResVo.builder()
                .mid("testMid")
                .ediDate("20260612120000")
                .signData("testSignData")
                .build();
        NicepayPaymentSignatureResDto resDto = NicepayPaymentSignatureResDto.builder()
                .mid("testMid")
                .ediDate("20260612120000")
                .signData("testSignData")
                .build();
        given(nicepayPaymentSignatureQueryMapper.mapToQuery(any(), any())).willReturn(query);
        given(paymentQueryService.requestPaymentSignature(any())).willReturn(resVo);
        given(nicepayPaymentSignatureQueryMapper.mapToDto(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.get(PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Nicepay.NICEPAY_SIGNATURE_URL)
                        .param("price", "10000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }

    @Test
    @DisplayName("나이스페이 결제 데이터 저장에 성공한다.")
    void requestCreatePayment_success() throws Exception {
        //  given
        NicepayPaymentCreateReqDto reqDto = new NicepayPaymentCreateReqDto(
                "testOrderNo", "testAuthToken", "testSignature", "testTransactionId",
                "http://approval.url", "http://network-cancel.url", 10000L
        );
        NicepayPaymentCreateCommand command = NicepayPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.NICEPAY)
                .orderNo("testOrderNo")
                .price(10000L)
                .build();
        NicepayPaymentCreateResVo resVo = NicepayPaymentCreateResVo.builder().paymentNo("testPaymentNo").build();
        NicepayPaymentCreateResDto resDto = new NicepayPaymentCreateResDto("testPaymentNo");
        given(nicepayPaymentCreateCommandMapper.mapToCommand(any(), any())).willReturn(command);
        given(paymentCommandService.createPayment(any())).willReturn(resVo);
        given(nicepayPaymentCreateCommandMapper.mapToDto(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.post(PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Nicepay.NICEPAY_URL)
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }
}
