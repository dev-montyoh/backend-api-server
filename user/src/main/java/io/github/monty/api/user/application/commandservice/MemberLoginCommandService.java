package io.github.monty.api.user.application.commandservice;

import io.github.monty.api.user.domain.model.command.MemberLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.domain.service.AuthCreateTokenService;
import io.github.monty.api.user.domain.service.MemberVerifyPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginCommandService {

    private final MemberVerifyPasswordService memberVerifyPasswordService;
    private final AuthCreateTokenService authCreateTokenService;

    /**
     * 로그인 애플리케이션 서비스
     * 비밀번호를 검증하고, 토큰 발급 요청을 한다.
     *
     * @param memberLoginCommand 로그인 요청 Command
     * @return 발급된 토큰 값
     */
    public AuthCreateTokenVo login(MemberLoginCommand memberLoginCommand) {
        memberVerifyPasswordService.verifyPassword(memberLoginCommand.getLoginId(), memberLoginCommand.getPassword());
        return authCreateTokenService.getTokens(memberLoginCommand.getLoginId());
    }
}
