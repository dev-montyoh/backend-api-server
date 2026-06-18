package dev.montyoh.auth.domain.service;

import dev.montyoh.auth.common.utils.JwtUtils;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCreateMcpTokenService {

    private final JwtUtils jwtUtils;

    /**
     * MCP 전용 장기 토큰을 발급하고 반환한다.
     *
     * @param userNo 회원 번호
     * @return MCP 토큰
     */
    public AuthCreateMcpTokenVo createMcpToken(String userNo) {
        return AuthCreateMcpTokenVo.builder()
                .token(jwtUtils.createMcpToken(userNo))
                .build();
    }
}
