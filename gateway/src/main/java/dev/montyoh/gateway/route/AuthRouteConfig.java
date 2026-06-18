package dev.montyoh.gateway.route;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRouteConfig {

    /**
     * auth 서비스는 내부 서비스(user)에서만 직접 호출하므로 외부 라우팅을 제공하지 않는다.
     */
}
