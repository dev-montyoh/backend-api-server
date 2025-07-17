package io.github.monty.api.user.interfaces.rest.controller;

import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = UserApiUrl.MONITOR_HEALTHCHECK_BASE_URL)
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("health checked", HttpStatus.OK);
    }
}
