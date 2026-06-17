package dev.montyoh.user.domain.model.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateCommand {

    private String loginId;
    private String password;
}
