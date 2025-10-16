package io.github.monty.api.user.domain.model.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginCommand {

    private String loginId;

    private String password;
}
