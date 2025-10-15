package io.github.monty.api.user.domain.service;

import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.common.utils.EncryptUtil;
import io.github.monty.api.user.domain.model.aggregate.Member;
import io.github.monty.api.user.domain.repository.UserRepository;
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
        Member member = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER_INFO));

        if (!EncryptUtil.match(password, member.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
