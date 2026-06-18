package dev.montyoh.auth.application.commandservice;

import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.domain.service.AuthCreateMcpTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthMcpTokenCommandService {

    private final AuthCreateMcpTokenService authCreateMcpTokenService;

    /**
     * MCP 토큰 발급 애플리케이션 서비스
     *
     * @param command MCP 토큰 발급 커맨드
     * @return MCP 토큰 발급 결과
     */
    public AuthCreateMcpTokenVo createMcpToken(AuthCreateMcpTokenCommand command) {
        return authCreateMcpTokenService.createMcpToken(command.getUserNo());
    }
}
