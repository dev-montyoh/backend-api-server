package io.github.monty.api.auth.interfaces.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl.AUTH_BASE_URL;
import static io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl.AUTH_HEALTH_CHECK_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(value = AUTH_BASE_URL)
@Tag(name = "Auth System API", description = "시스템 관련 API")
public class SystemController {

    @Operation(summary = "상태 확인 API", description = "해당 서비스의 상태를 확인한다.")
    @GetMapping(value = AUTH_HEALTH_CHECK_BASE_URL)
    public String healthCheck() {
        return "health checked";
    }
}
