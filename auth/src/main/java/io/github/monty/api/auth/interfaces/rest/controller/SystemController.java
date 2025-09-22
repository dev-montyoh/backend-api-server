package io.github.monty.api.auth.interfaces.rest.controller;

import io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = AuthApiUrl.AUTH_V1_BASE_URL)
@Tag(name = "Auth System API", description = "시스템 관련 API")
public class SystemController {

    @Operation(summary = "상태 확인 API", description = "해당 서비스의 상태를 확인한다.")
    @GetMapping(value = AuthApiUrl.System.HEALTH_CHECK_URL)
    public String healthCheck() {
        return "health checked";
    }
}
