package dev.montyoh.user.infrastructure.repository.feign;

import dev.montyoh.user.common.configuration.FeignConfig;
import dev.montyoh.user.infrastructure.repository.feign.constants.AuthUrl;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenRspDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "AuthFeignClient", url = "${feign.api.auth-url}", configuration = FeignConfig.class)
public interface AuthFeignClient {

    /**
     * Auth 애플리케이션에게 토큰 생성 요청을 한다.
     *
     * @param authCreateTokenReqDto 로그인 요청이 들어온 userNo 이 담긴 ReqDto
     * @return 토큰 생성 결과
     */
    @PostMapping(AuthUrl.AUTH_CREATE_TOKEN)
    ResponseEntity<AuthCreateTokenRspDto> createAccessTokenAndRefreshToken(AuthCreateTokenReqDto authCreateTokenReqDto);

    /**
     * Auth 애플리케이션에게 MCP 토큰 생성 요청을 한다.
     *
     * @param authCreateMcpTokenReqDto userNo 이 담긴 ReqDto
     * @return MCP 토큰 생성 결과
     */
    @PostMapping(AuthUrl.AUTH_CREATE_MCP_TOKEN)
    ResponseEntity<AuthCreateMcpTokenRspDto> createMcpToken(AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto);
}
