package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.application.PaymentCommandService;
import io.github.monty.api.payment.application.PaymentQueryService;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.InicisPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCreateResultVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentSignatureResultVO;
import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateRequest;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentSignatureResponse;
import io.github.monty.api.payment.interfaces.rest.dto.InicisPaymentCreateResponse;
import io.github.monty.api.payment.interfaces.rest.mapper.InicisPaymentCreateCommandMapper;
import io.github.monty.api.payment.interfaces.rest.mapper.InicisPaymentSignatureQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Inicis Payment API", description = "결제 API")
public class InicisPaymentController {

    private final InicisPaymentSignatureQueryMapper inicisPaymentSignatureQueryMapper;
    private final InicisPaymentCreateCommandMapper inicisPaymentCreateCommandMapper;

    private final PaymentQueryService paymentQueryService;
    private final PaymentCommandService paymentCommandService;

    @Operation(summary = "이니시스 결제 인증 정보 획득 API", description = "이니시스 결제 인증 단계에 필요한 정보를 획득한다.")
    @GetMapping(value = PaymentApiUrl.Inicis.INICIS_SIGNATURE_URL)
    public ResponseEntity<InicisPaymentSignatureResponse> requestPaymentSignature(@RequestParam String oid,
                                                                                  @RequestParam String price) {
        InicisPaymentSignatureQuery inicisPaymentSignatureQuery = inicisPaymentSignatureQueryMapper.mapToQuery(oid, price, PaymentType.INICIS);
        InicisPaymentSignatureResultVO inicisPaymentSignatureResultVO = (InicisPaymentSignatureResultVO) paymentQueryService.requestPaymentSignature(inicisPaymentSignatureQuery);
        InicisPaymentSignatureResponse inicisPaymentSignatureResponse = inicisPaymentSignatureQueryMapper.mapToDTO(inicisPaymentSignatureResultVO);
        return ResponseEntity.ok().body(inicisPaymentSignatureResponse);
    }

    @Operation(summary = "이니시스 결제 데이터 저장 API", description = "이니시스 결제 인증 결과를 저장한다.")
    @PostMapping(value = PaymentApiUrl.Inicis.INICIS_URL)
    public ResponseEntity<InicisPaymentCreateResponse> requestCreatePayment(@RequestBody InicisPaymentCreateRequest inicisPaymentCreateRequest) {
        InicisPaymentCreateCommand inicisPaymentCreateCommand = inicisPaymentCreateCommandMapper.mapToCommand(inicisPaymentCreateRequest, PaymentType.INICIS);
        InicisPaymentCreateResultVO inicisPaymentCreateResultVO = (InicisPaymentCreateResultVO) paymentCommandService.createPayment(inicisPaymentCreateCommand);
        InicisPaymentCreateResponse inicisPaymentCreateResponse = inicisPaymentCreateCommandMapper.mapToDTO(inicisPaymentCreateResultVO);
        return ResponseEntity.ok().body(inicisPaymentCreateResponse);
    }
}
