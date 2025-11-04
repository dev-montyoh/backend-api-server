package io.github.monty.api.payment.interfaces.rest.controller;

import io.github.monty.api.payment.interfaces.rest.constants.PaymentApiUrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiUrl.PAYMENT_BASE_URL)
@Tag(name = "System API", description = "시스템 API")
public class SystemController {

    @Operation(summary = "HealthCheck API", description = "애플리케이션 상태 확인 API")
    @GetMapping(value = PaymentApiUrl.System.HEALTH_CHECK_URL)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
