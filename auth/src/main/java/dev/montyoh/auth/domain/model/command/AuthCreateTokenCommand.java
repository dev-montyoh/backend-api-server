package dev.montyoh.auth.domain.model.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCreateTokenCommand {
    private String userNo;
}
