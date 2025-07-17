package io.github.monty.api.auth.application.commandservice;

import io.github.monty.api.auth.domain.model.command.AuthCreateTokenCommand;
import io.github.monty.api.auth.domain.model.command.AuthRefreshTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.domain.service.AuthCreateTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenCommandService {

    private final AuthCreateTokenService authCreateTokenService;

    /**
     * 인증에 필요한 토큰을 생성하고 반환한다.
     *
     * @param authCreateTokenCommand 토큰 생성 커맨드
     * @return 토큰 생성 결과
     */
    public AuthCreateTokenVo createToken(AuthCreateTokenCommand authCreateTokenCommand) {
        return authCreateTokenService.createTokenAndSaveRefreshToken(authCreateTokenCommand.getUserNo());
    }

    /**
     * 인증에 필요한 토큰을 리프레시토큰을 사용하여 새롭게 생성하고 반환한다.
     *
     * @param authRefreshTokenCommand 토큰 리프레시 커맨드
     * @return 토큰 리프레시 결과
     */
    public AuthRefreshTokenVo refreshToken(AuthRefreshTokenCommand authRefreshTokenCommand) {
        return authCreateTokenService.refreshToken(authRefreshTokenCommand.getRefreshToken());
    }
}
