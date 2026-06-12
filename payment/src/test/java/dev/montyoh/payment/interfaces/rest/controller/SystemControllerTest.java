package dev.montyoh.payment.interfaces.rest.controller;

import dev.montyoh.payment.interfaces.rest.constants.PaymentApiUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemController.class)
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("HealthCheck API 요청에 성공한다.")
    void healthCheck_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PaymentApiUrl.PAYMENT_BASE_URL + PaymentApiUrl.System.HEALTH_CHECK_URL))
                .andExpect(status().isOk());
    }
}
