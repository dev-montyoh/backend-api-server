package dev.montyoh.gateway.config;

import dev.montyoh.gateway.common.property.WhiteListProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private static final String MCP_BASE_PATH = "/api/mcp/**";

    private final SpringSecurityAuthenticationConverter springSecurityAuthenticationConverter;
    private final SpringSecurityAuthenticationManager springSecurityAuthenticationManager;
    private final McpAuthenticationManager mcpAuthenticationManager;

    private final WhiteListProperties whiteListProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        // 기존 필터: MCP 경로 제외
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(springSecurityAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(springSecurityAuthenticationConverter);
        authenticationWebFilter.setRequiresAuthenticationMatcher(
                new NegatedServerWebExchangeMatcher(new PathPatternParserServerWebExchangeMatcher(MCP_BASE_PATH))
        );

        // MCP 전용 필터
        AuthenticationWebFilter mcpAuthenticationWebFilter = new AuthenticationWebFilter(mcpAuthenticationManager);
        mcpAuthenticationWebFilter.setServerAuthenticationConverter(springSecurityAuthenticationConverter);
        mcpAuthenticationWebFilter.setRequiresAuthenticationMatcher(
                new PathPatternParserServerWebExchangeMatcher(MCP_BASE_PATH)
        );

        serverHttpSecurity
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(mcpAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        ;

        //  TODO: 추후 상세히 작업할 것. 지금은 false
        //  cors, csrf 설정
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
        ;

        //  URI : ROLE 인가 매칭 설정
        serverHttpSecurity
                .authorizeExchange(
                        exchange -> exchange
                                .pathMatchers(whiteListProperties.toArray()).permitAll()
                                .anyExchange().authenticated()
                )
        ;

        return serverHttpSecurity.build();
    }
}
