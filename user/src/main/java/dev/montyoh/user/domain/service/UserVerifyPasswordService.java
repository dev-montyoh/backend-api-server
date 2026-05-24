package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.common.utils.EncryptUtil;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVerifyPasswordService {

    private final UserRepository userRepository;

    /**
     * userId 와 password 를 비교해서 password 가 일치하는지 확인한다.
     *
     * @param loginId  로그인 Id
     * @param password 비밀번호
     */
    public void verifyPassword(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER_INFO));

        if (!EncryptUtil.match(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
