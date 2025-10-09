package io.github.monty.api.gateway.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserRouteConfig {

    @Value("${mapping.url.user}")
    private String userUrl;

    /**
     * user 서비스 대응 라우터
     */
    @Bean
    public RouteLocator userRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        route -> route
                                .path("/user/**")
                                .uri(userUrl)
                )
                .build()
                ;
    }
}
