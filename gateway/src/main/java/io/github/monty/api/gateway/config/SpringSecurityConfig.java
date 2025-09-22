package io.github.monty.api.gateway.config;

import io.github.monty.api.gateway.common.property.WhiteListProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SpringSecurityAuthenticationConverter springSecurityAuthenticationConverter;
    private final SpringSecurityAuthenticationManager springSecurityAuthenticationManager;

    private final WhiteListProperties whiteListProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(springSecurityAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(springSecurityAuthenticationConverter);

        //  인증 매니저 설정
        serverHttpSecurity
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
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
