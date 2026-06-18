package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.domain.repository.AuthRepository;
import dev.montyoh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCreateMcpTokenService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    /**
     * 회원 번호를 조회한 후 MCP 토큰 생성 요청을 한다.
     *
     * @param loginId 로그인 아이디
     * @return MCP 토큰 생성 결과
     */
    public AuthCreateMcpTokenVo getMcpToken(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER_INFO));

        return authRepository.createMcpToken(user.getUserNo());
    }
}
