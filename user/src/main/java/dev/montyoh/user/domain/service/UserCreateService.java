package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.common.utils.EncryptUtil;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserCreateService {

    private final UserRepository userRepository;

    /**
     * 신규 유저를 생성한다.
     * loginId 중복 여부를 확인하고, 비밀번호를 암호화하여 저장한다.
     *
     * @param command 유저 생성 Command
     */
    public void create(UserCreateCommand command) {
        if (userRepository.existsByLoginId(command.getLoginId())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_USER_INFO);
        }

        User user = User.builder()
                .userNo(generateUserNo())
                .loginId(command.getLoginId())
                .password(EncryptUtil.encode(command.getPassword()))
                .build();
        userRepository.save(user);
    }

    /**
     * UUID 기반의 사용자 번호를 생성한다.
     *
     * @return 20자 이내의 사용자 번호
     */
    private String generateUserNo() {
        UUID uuid = UUID.randomUUID();
        String base36 = new BigInteger(uuid.toString().replace("-", ""), 16).toString(36);
        return base36.length() > 20 ? base36.substring(0, 20) : base36;
    }
}
