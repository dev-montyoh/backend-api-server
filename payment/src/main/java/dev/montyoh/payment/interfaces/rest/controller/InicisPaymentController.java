package dev.montyoh.payment.interfaces.rest.controller;

import dev.montyoh.payment.application.PaymentCommandService;
import dev.montyoh.payment.application.PaymentQueryService;
import dev.montyoh.payment.common.constants.PaymentServiceProviderType;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Inicis Payment API", description = "이니시스 결제 API")
public class InicisPaymentController {

    private final InicisPaymentSignatureQueryMapper inicisPaymentSignatureQueryMapper;
    private final InicisPaymentCreateCommandMapper inicisPaymentCreateCommandMapper;

    private final PaymentQueryService paymentQueryService;
    private final PaymentCommandService paymentCommandService;

    @Operation(summary = "이니시스 결제 인증 정보 획득 API", description = "이니시스 결제 인증 단계에 필요한 정보를 획득한다.")
    @GetMapping(value = PaymentApiUrl.Inicis.INICIS_SIGNATURE_URL)
    public ResponseEntity<InicisPaymentSignatureResDto> requestPaymentSignature(@RequestParam String oid,
                                                                                @RequestParam String price) {
        InicisPaymentSignatureQuery inicisPaymentSignatureQuery = inicisPaymentSignatureQueryMapper.mapToQuery(oid, price, PaymentServiceProviderType.INICIS);
        InicisPaymentSignatureResVo inicisPaymentSignatureResVo = (InicisPaymentSignatureResVo) paymentQueryService.requestPaymentSignature(inicisPaymentSignatureQuery);
        InicisPaymentSignatureResDto inicisPaymentSignatureResDto = inicisPaymentSignatureQueryMapper.mapToDTO(inicisPaymentSignatureResVo);
        return ResponseEntity.ok().body(inicisPaymentSignatureResDto);
    }

    @Operation(summary = "이니시스 결제 데이터 저장 API", description = "이니시스 결제 인증 결과를 저장한다.")
    @PostMapping(value = PaymentApiUrl.Inicis.INICIS_URL)
    public ResponseEntity<InicisPaymentCreateResDto> requestCreatePayment(@RequestBody InicisPaymentCreateReqDto inicisPaymentCreateReqDto) {
        InicisPaymentCreateCommand inicisPaymentCreateCommand = inicisPaymentCreateCommandMapper.mapToCommand(inicisPaymentCreateReqDto, PaymentServiceProviderType.INICIS);
        InicisPaymentCreateResVo inicisPaymentCreateResVo = (InicisPaymentCreateResVo) paymentCommandService.createPayment(inicisPaymentCreateCommand);
        InicisPaymentCreateResDto inicisPaymentCreateResDto = inicisPaymentCreateCommandMapper.mapToDTO(inicisPaymentCreateResVo);
        return ResponseEntity.ok().body(inicisPaymentCreateResDto);
    }
}
