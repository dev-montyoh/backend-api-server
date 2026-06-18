package dev.montyoh.gateway.route;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class AuthRouteConfig {

    @Value("${mapping.url.auth}")
    private String authUrl;

    /**
     * auth 서비스 라우터
     * 토큰 갱신(PUT /api/auth/v1/token)만 허용한다.
     * MCP 토큰 발급(POST /auth/v1/mcp/token)은 user 서비스 내부에서만 호출하므로 외부 노출하지 않는다.
     */
    @Bean
    public RouteLocator authRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        route -> route
                                .path("/api/auth/v1/token")
                                .and()
                                .method(HttpMethod.PUT)
                                .filters(f -> f.rewritePath(
                                        "/api/auth/(?<segment>.*)",
                                        "/auth/${segment}"
                                ))
                                .uri(authUrl)
                )
                .build();
    }
}
