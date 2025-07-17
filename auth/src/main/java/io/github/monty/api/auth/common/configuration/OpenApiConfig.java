package io.github.monty.api.auth.common.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info();
        openAPI.info(
                info.title("Backend Api Server Auth API")
                        .version("version 0.0.1")
                        .description("Monty 개인 API Server Auth 서비스")
        );
        return openAPI;
    }
}
