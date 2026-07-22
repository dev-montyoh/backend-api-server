package dev.montyoh.user.application.commandservice;

import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.domain.service.UserCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreateCommandService {

    private final UserCreateService userCreateService;

    /**
     * 유저 생성 애플리케이션 서비스
     *
     * @param command 유저 생성 Command
     * @return 생성된 User
     */
    public User create(UserCreateCommand command) {
        return userCreateService.create(command);
    }
}
