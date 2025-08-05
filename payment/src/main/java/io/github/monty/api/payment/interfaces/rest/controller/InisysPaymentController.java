package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.application.PaymentQueryService;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.domain.model.query.PaymentInfoQuery;
import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.github.monty.api.payment.interfaces.rest.dto.PaymentAuthInfoResDto;
import io.github.monty.api.payment.interfaces.rest.mapper.PaymentInfoQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_URL)
@Tag(name = "Inisys Payment API", description = "결제 API")
public class InisysPaymentController {

    private final PaymentInfoQueryMapper paymentInfoQueryMapper;
    private final PaymentQueryService paymentQueryService;

    @Operation(summary = "이니시스 결제 인증 정보 획득 API", description = "이니시스 결제 인증 단계에 필요한 정보를 획득한다.")
    @GetMapping(value = PaymentApiUrl.Inisys.AUTH_INFO_URL)
    public ResponseEntity<PaymentAuthInfoResDto> requestPaymentInfo(@RequestParam String oid,
                                                                    @RequestParam String price) {
        PaymentInfoQuery paymentInfoQuery = paymentInfoQueryMapper.mapToQuery(oid, price, PaymentType.INISYS);
        PaymentAuthInfoResDto paymentAuthInfoResDto = paymentQueryService.requestPaymentInfo(paymentInfoQuery);
        return ResponseEntity.ok().body(paymentAuthInfoResDto);
    }
}
