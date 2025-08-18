package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.application.PaymentQueryService;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.command.InisysPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.InisysPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.InisysPaymentSignatureResultResultVO;
import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentCreateRequest;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentCreateResponse;
import io.github.monty.api.payment.interfaces.rest.dto.InisysPaymentSignatureResponse;
import io.github.monty.api.payment.interfaces.rest.mapper.InisysPaymentCreateCommandMapper;
import io.github.monty.api.payment.interfaces.rest.mapper.InisysPaymentSignatureQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Inisys Payment API", description = "결제 API")
public class InisysPaymentController {

    private final InisysPaymentSignatureQueryMapper inisysPaymentSignatureQueryMapper;
    private final InisysPaymentCreateCommandMapper inisysPaymentCreateCommandMapper;

    private final PaymentQueryService paymentQueryService;

    @Operation(summary = "이니시스 결제 인증 정보 획득 API", description = "이니시스 결제 인증 단계에 필요한 정보를 획득한다.")
    @GetMapping(value = PaymentApiUrl.Inisys.INISYS_SIGNATURE_URL)
    public ResponseEntity<InisysPaymentSignatureResponse> requestPaymentSignature(@RequestParam String oid,
                                                                                  @RequestParam String price) {
        InisysPaymentSignatureQuery inisysPaymentSignatureQuery = inisysPaymentSignatureQueryMapper.mapToQuery(oid, price, PaymentType.INISYS);
        InisysPaymentSignatureResultResultVO inisysPaymentSignatureResultVO = (InisysPaymentSignatureResultResultVO) paymentQueryService.requestPaymentSignature(inisysPaymentSignatureQuery);
        InisysPaymentSignatureResponse inisysPaymentSignatureResponse = inisysPaymentSignatureQueryMapper.mapToDTO(inisysPaymentSignatureResultVO);
        return ResponseEntity.ok().body(inisysPaymentSignatureResponse);
    }

    @Operation(summary = "이니시스 결제 데이터 저장 API", description = "이니시스 결제 인증 결과를 저장한다.")
    @PostMapping(value = PaymentApiUrl.Inisys.INISYS_URL)
    public ResponseEntity<InisysPaymentCreateResponse> requestCreatePayment(@RequestBody InisysPaymentCreateRequest inisysPaymentCreateRequest) {
        InisysPaymentCreateCommand inisysPaymentCreateCommand = inisysPaymentCreateCommandMapper.mapToCommand(inisysPaymentCreateRequest, PaymentType.INISYS);

        return ResponseEntity.ok().build();
    }
}
