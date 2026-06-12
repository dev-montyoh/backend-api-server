package dev.montyoh.payment.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.payment.application.PaymentCommandService;
import dev.montyoh.payment.application.PaymentLogQueryService;
import dev.montyoh.payment.application.PaymentQueryService;
import dev.montyoh.payment.domain.model.command.PaymentApprovalCommand;
import dev.montyoh.payment.domain.model.command.PaymentCancelCommand;
import dev.montyoh.payment.domain.model.command.PaymentNetworkCancelCommand;
import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.interfaces.rest.constants.PaymentApiUrl;
import dev.montyoh.payment.interfaces.rest.dto.PaymentCancelReqDto;
import dev.montyoh.payment.interfaces.rest.dto.PaymentListResDto;
import dev.montyoh.payment.interfaces.rest.dto.PaymentLogListResDto;
import dev.montyoh.payment.interfaces.rest.mapper.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PaymentControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentListQueryMapper paymentListQueryMapper;
    @MockBean
    private PaymentLogListQueryMapper paymentLogListQueryMapper;
    @MockBean
    private PaymentApprovalCommandMapper paymentApprovalCommandMapper;
    @MockBean
    private PaymentCancelCommandMapper paymentCancelCommandMapper;
    @MockBean
    private PaymentNetworkCancelCommandMapper paymentNetworkCancelCommandMapper;
    @MockBean
    private PaymentQueryService paymentQueryService;
    @MockBean
    private PaymentCommandService paymentCommandService;
    @MockBean
    private PaymentLogQueryService paymentLogQueryService;

    @Test
    @DisplayName("결제 승인 요청에 성공한다.")
    void requestApprovePayment_success() throws Exception {
        //  given
        String paymentNo = "testPaymentNo";
        given(paymentApprovalCommandMapper.mapToCommand(paymentNo))
                .willReturn(PaymentApprovalCommand.builder().paymentNo(paymentNo).build());
        willDoNothing().given(paymentCommandService).approvePayment(any());

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.post(
                        PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Payment.PAYMENT_APPROVAL_URL, paymentNo))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 취소 요청에 성공한다.")
    void requestCancelPayment_success() throws Exception {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentCancelReqDto reqDto = new PaymentCancelReqDto("테스트 취소");
        given(paymentCancelCommandMapper.mapToCommand(any(), any()))
                .willReturn(PaymentCancelCommand.builder().paymentNo(paymentNo).cancelReason("테스트 취소").build());
        willDoNothing().given(paymentCommandService).cancelPayment(any());

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.post(
                        PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Payment.PAYMENT_CANCEL_URL, paymentNo)
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 망취소 요청에 성공한다.")
    void requestNetCancelPayment_success() throws Exception {
        //  given
        String paymentNo = "testPaymentNo";
        given(paymentNetworkCancelCommandMapper.mapToCommand(paymentNo))
                .willReturn(PaymentNetworkCancelCommand.builder().paymentNo(paymentNo).build());
        willDoNothing().given(paymentCommandService).networkCancelPayment(any());

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.post(
                        PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Payment.PAYMENT_NET_CANCEL_URL, paymentNo))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 목록 조회 요청에 성공한다.")
    void requestPaymentList_success() throws Exception {
        //  given
        PaymentListQuery query = PaymentListQuery.builder().pageable(PageRequest.of(0, 50)).build();
        PaymentListResVo resVo = PaymentListResVo.builder().paymentList(List.of()).totalPages(0L).totalCount(0L).build();
        PaymentListResDto resDto = new PaymentListResDto(List.of(), 0L, 0L);
        given(paymentListQueryMapper.mapToQuery(any(), any())).willReturn(query);
        given(paymentQueryService.requestPaymentList(any())).willReturn(resVo);
        given(paymentListQueryMapper.mapToDto(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.get(PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Payment.PAYMENT_LIST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }

    @Test
    @DisplayName("결제 로그 목록 조회 요청에 성공한다.")
    void requestPaymentLogList_success() throws Exception {
        //  given
        String paymentNo = "testPaymentNo";
        PaymentLogListQuery query = PaymentLogListQuery.builder().paymentNo(paymentNo).pageable(PageRequest.of(0, 50)).build();
        PaymentLogListResVo resVo = PaymentLogListResVo.builder().paymentLogList(List.of()).totalPages(0L).totalCount(0L).build();
        PaymentLogListResDto resDto = PaymentLogListResDto.builder().paymentLogList(List.of()).totalPages(0L).totalCount(0L).build();
        given(paymentLogListQueryMapper.mapToQuery(any(String.class), any(Long.class), any(Long.class))).willReturn(query);
        given(paymentLogQueryService.requestPaymentLogList(any())).willReturn(resVo);
        given(paymentLogListQueryMapper.mapToDto(any())).willReturn(resDto);

        //  when, then
        mockMvc.perform(MockMvcRequestBuilders.get(
                        PaymentApiUrl.PAYMENT_URL + PaymentApiUrl.Payment.PAYMENT_LOG_LIST_URL, paymentNo))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(resDto)));
    }
}
