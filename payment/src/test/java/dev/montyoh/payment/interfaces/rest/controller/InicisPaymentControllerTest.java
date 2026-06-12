package dev.montyoh.payment.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.payment.application.PaymentCommandService;
import dev.montyoh.payment.application.PaymentQueryService;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.query.InicisPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.InicisPaymentCreateResVo;
import dev.montyoh.payment.domain.model.vo.InicisPaymentSignatureResVo;
import dev.montyoh.payment.interfaces.rest.constants.PaymentApiUrl;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateReqDto;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentCreateResDto;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentSignatureResDto;
import dev.montyoh.payment.interfaces.rest.mapper.InicisPaymentCreateCommandMapper;
import dev.montyoh.payment.interfaces.rest.mapper.InicisPaymentSignatureQueryMapper;
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

@WebMvcTest(InicisPaymentController.class)
class InicisPaymentControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InicisPaymentSignatureQueryMapper inicisPaymentSignatureQueryMapper;
    @MockBean
    private InicisPaymentCreateCommandMapper inicisPaymentCreateCommandMapper;
    @MockBean
    private PaymentQueryService paymentQueryService;
    @MockBean
    private PaymentCommandService paymentCommandService;

    @Test
    @DisplayName("이니시스 결제 인증 정보 획득에 성공한다.")
    void requestPaymentSignature_success() throws Exception {
        //  given
        InicisPaymentSignatureQuery query = InicisPaymentSignatureQuery.builder()
                .pgProviderType(PgProviderType.INICIS)
                .oid("testOid")
                .price("10000")
                .build();
        InicisPaymentSignatureResVo resVo = InicisPaymentSignatureResVo.builder()
                .signature("testSignature")
                .verification("testVerification")
                .mKey("testMKey")
                .mid("testMid")
                .timestamp(12345678L)
                .build();
        InicisPaymentSignatureResDto resDto = InicisPaymentSignatureResDto.builder()
                .signature("testSignature")
                .verification("testVerification")
                .mKey("testMKey")
                .mid("testMid")
                .timestamp(12345678L)
                .build();
        given(inicisPaymentSignatureQueryMapper.mapToQuery(any(), any(), any())).willReturn(query);
        given(paymentQueryService.requestPaymentSignature(any())).willReturn(resVo);
        given(inicisPaymentSignatureQueryMapper.mapToDTO(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.get(PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Inicis.INICIS_SIGNATURE_URL)
                        .param("oid", "testOid")
                        .param("price", "10000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }

    @Test
    @DisplayName("이니시스 결제 데이터 저장에 성공한다.")
    void requestCreatePayment_success() throws Exception {
        //  given
        InicisPaymentCreateReqDto reqDto = new InicisPaymentCreateReqDto(
                "testMid", "testOrderNo", "testAuthToken", "testIdcCode",
                "http://approval.url", "http://network-cancel.url", 10000L
        );
        InicisPaymentCreateCommand command = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .build();
        InicisPaymentCreateResVo resVo = InicisPaymentCreateResVo.builder().paymentNo("testPaymentNo").build();
        InicisPaymentCreateResDto resDto = InicisPaymentCreateResDto.builder().paymentNo("testPaymentNo").build();
        given(inicisPaymentCreateCommandMapper.mapToCommand(any(), any())).willReturn(command);
        given(paymentCommandService.createPayment(any())).willReturn(resVo);
        given(inicisPaymentCreateCommandMapper.mapToDTO(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.post(PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Inicis.INICIS_URL)
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }
}
