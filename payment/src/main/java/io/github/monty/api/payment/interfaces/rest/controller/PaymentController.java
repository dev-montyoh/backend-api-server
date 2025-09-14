package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.application.PaymentCommandService;
import io.github.monty.api.payment.domain.model.command.PaymentApprovalCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.domain.model.command.PaymentNetworkCancelCommand;
import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentCancelRequest;
import io.github.monty.api.payment.interfaces.rest.mapper.PaymentApprovalCommandMapper;
import io.github.monty.api.payment.interfaces.rest.mapper.PaymentCancelCommandMapper;
import io.github.monty.api.payment.interfaces.rest.mapper.PaymentNetworkCancelCommandMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Payment API", description = "결제 API")
public class PaymentController {

    private final PaymentApprovalCommandMapper paymentApprovalCommandMapper;
    private final PaymentCancelCommandMapper paymentCancelCommandMapper;
    private final PaymentNetworkCancelCommandMapper paymentNetworkCancelCommandMapper;

    private final PaymentCommandService paymentCommandService;

    @Operation(summary = "결제 승인 요청 API", description = "해당 결제 번호의 승인 요청을 한다.")
    @PostMapping(value = PaymentApiUrl.Payment.PAYMENT_APPROVAL_URL)
    public ResponseEntity<Void> requestApprovePayment(@PathVariable String paymentNo) {
        PaymentApprovalCommand paymentApprovalCommand = paymentApprovalCommandMapper.mapToCommand(paymentNo);
        paymentCommandService.approvePayment(paymentApprovalCommand);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제 취소 요청 API", description = "해당 결제 번호의 취소 요청을 한다.")
    @PostMapping(value = PaymentApiUrl.Payment.PAYMENT_CANCEL_URL)
    public ResponseEntity<Void> requestCancelPayment(@PathVariable String paymentNo, @RequestBody PaymentCancelRequest paymentCancelRequest) {
        PaymentCancelCommand paymentCancelCommand = paymentCancelCommandMapper.mapToCommand(paymentCancelRequest, paymentNo);
        paymentCommandService.cancelPayment(paymentCancelCommand);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제 망취소 요청 API", description = "해당 결제 번호의 망취소 요청을 한다. (인증결과 응답 후 10분 이내) (일반 결제 취소 용도로 사용 금지)")
    @PostMapping(value = PaymentApiUrl.Payment.PAYMENT_NET_CANCEL_URL)
    public ResponseEntity<Void> requestNetCancelPayment(@PathVariable String paymentNo) {
        PaymentNetworkCancelCommand paymentNetworkCancelCommand = paymentNetworkCancelCommandMapper.mapToCommand(paymentNo);
        paymentCommandService.networkCancelPayment(paymentNetworkCancelCommand);
        return ResponseEntity.ok().build();
    }
}
