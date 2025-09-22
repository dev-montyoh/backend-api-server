package io.github.monty.api.auth.interfaces.rest.controller;

import io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemController.class)
class SystemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("HealthCheck API 요청에 성공한다.")
    void HealthCheck_API_call_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.System.HEALTH_CHECK_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("health checked"))
        ;
    }
}