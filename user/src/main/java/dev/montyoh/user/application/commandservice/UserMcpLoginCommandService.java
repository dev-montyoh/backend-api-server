package dev.montyoh.user.application.commandservice;

import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.domain.service.AuthCreateMcpTokenService;
import dev.montyoh.user.domain.service.UserVerifyPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMcpLoginCommandService {

    private final UserVerifyPasswordService userVerifyPasswordService;
    private final AuthCreateMcpTokenService authCreateMcpTokenService;

    /**
     * MCP 로그인 애플리케이션 서비스
     * 비밀번호를 검증하고, MCP 토큰 발급 요청을 한다.
     *
     * @param userLoginCommand 로그인 요청 Command
     * @return 발급된 MCP 토큰 값
     */
    public AuthCreateMcpTokenVo login(UserLoginCommand userLoginCommand) {
        userVerifyPasswordService.verifyPassword(userLoginCommand.getLoginId(), userLoginCommand.getPassword());
        return authCreateMcpTokenService.getMcpToken(userLoginCommand.getLoginId());
    }
}
