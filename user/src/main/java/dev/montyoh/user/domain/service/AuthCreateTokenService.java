package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.user.domain.repository.AuthRepository;
import dev.montyoh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCreateTokenService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    /**
     * 회원 번호를 조회한 후
     * AccessToken, RefreshToken 생성 요청을 한다.
     * 생성 결과를 반환한다.
     *
     * @param loginId 로그인 아이디
     * @return 토큰 생성 결과
     */
    public AuthCreateTokenVo getTokens(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER_INFO));

        return authRepository.createAccessTokenAndRefreshToken(user.getUserNo());
    }
}
