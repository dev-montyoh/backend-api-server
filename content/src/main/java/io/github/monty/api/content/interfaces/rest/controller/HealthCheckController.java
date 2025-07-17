package io.github.monty.api.content.interfaces.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.monty.api.content.interfaces.rest.constants.ContentApiUrl.CONTENT_HEALTH_CHECK_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(value = CONTENT_HEALTH_CHECK_BASE_URL)
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "health checked";
    }
}
