package io.github.monty.api.user.application.commandservice;

import io.github.monty.api.user.domain.model.command.UserLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.domain.service.AuthCreateTokenService;
import io.github.monty.api.user.domain.service.UserVerifyPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginCommandService {

    private final UserVerifyPasswordService userVerifyPasswordService;
    private final AuthCreateTokenService authCreateTokenService;

    /**
     * 로그인 애플리케이션 서비스
     * 비밀번호를 검증하고, 토큰 발급 요청을 한다.
     *
     * @param userLoginCommand 로그인 요청 Command
     * @return 발급된 토큰 값
     */
    public AuthCreateTokenVo login(UserLoginCommand userLoginCommand) {
        userVerifyPasswordService.verifyPassword(userLoginCommand.getLoginId(), userLoginCommand.getPassword());
        return authCreateTokenService.getTokens(userLoginCommand.getLoginId());
    }
}
