package io.github.monty.api.gateway.route;

import io.github.monty.api.gateway.common.constant.ApiUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthRouteConfig {

    /**
     * auth 서비스 대응 라우터
     */
    @Bean
    public RouteLocator authRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        route -> route
                                .path("/auth/**")
                                .uri(ApiUrl.MAPPING_AUTH_URL)
                )
                .build()
                ;
    }
}
