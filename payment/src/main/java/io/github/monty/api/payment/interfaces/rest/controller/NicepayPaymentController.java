package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.application.PaymentCommandService;
import io.github.monty.api.payment.application.PaymentQueryService;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.domain.model.command.NicepayPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.NicepayPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCreateResVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentCreateReqDto;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentCreateResDto;
import io.github.monty.api.payment.interfaces.rest.dto.NicepayPaymentSignatureResDto;
import io.github.monty.api.payment.interfaces.rest.mapper.NicepayPaymentCreateCommandMapper;
import io.github.monty.api.payment.interfaces.rest.mapper.NicepayPaymentSignatureQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Nicepay Payment API", description = "나이스페이 결제 API")
public class NicepayPaymentController {

    private final NicepayPaymentSignatureQueryMapper nicepayPaymentSignatureQueryMapper;
    private final NicepayPaymentCreateCommandMapper nicepayPaymentCreateCommandMapper;

    private final PaymentQueryService paymentQueryService;
    private final PaymentCommandService paymentCommandService;

    @Operation(summary = "나이스페이 결제 인증 정보 획득 API", description = "나이스페이 결제 인증 단계에 필요한 정보를 획득한다.")
    @GetMapping(value = PaymentApiUrl.Nicepay.NICEPAY_SIGNATURE_URL)
    public ResponseEntity<NicepayPaymentSignatureResDto> requestPaymentSignature(@RequestParam String price) {
        NicepayPaymentSignatureQuery nicepayPaymentSignatureQuery = nicepayPaymentSignatureQueryMapper.mapToQuery(price, PaymentServiceProviderType.NICEPAY);
        NicepayPaymentSignatureResVo nicepayPaymentSignatureResVo = (NicepayPaymentSignatureResVo) paymentQueryService.requestPaymentSignature(nicepayPaymentSignatureQuery);
        NicepayPaymentSignatureResDto nicepayPaymentSignatureResDto = nicepayPaymentSignatureQueryMapper.mapToDto(nicepayPaymentSignatureResVo);
        return ResponseEntity.ok().body(nicepayPaymentSignatureResDto);
    }

    @Operation(summary = "나이스페이 결제 데이터 저장 API", description = "나이스페이 결제 인증 결과를 저장한다.")
    @PostMapping(value = PaymentApiUrl.Nicepay.NICEPAY_URL)
    public ResponseEntity<NicepayPaymentCreateResDto> requestCreatePayment(@RequestBody NicepayPaymentCreateReqDto nicepayPaymentCreateReqDto) {
        NicepayPaymentCreateCommand nicepayPaymentCreateCommand = nicepayPaymentCreateCommandMapper.mapToCommand(nicepayPaymentCreateReqDto, PaymentServiceProviderType.NICEPAY);
        NicepayPaymentCreateResVo nicepayPaymentCreateResVo = (NicepayPaymentCreateResVo) paymentCommandService.createPayment(nicepayPaymentCreateCommand);
        NicepayPaymentCreateResDto nicepayPaymentCreateResDto = nicepayPaymentCreateCommandMapper.mapToDto(nicepayPaymentCreateResVo);
        return ResponseEntity.ok().body(nicepayPaymentCreateResDto);
    }

}
