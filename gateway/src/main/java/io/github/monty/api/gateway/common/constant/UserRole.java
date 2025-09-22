package io.github.monty.api.gateway.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_GUEST("ROLE_GUEST", "GUEST", "for not authenticated user"),
    ROLE_MASTER("ROLE_MASTER", "MASTER", "for authenticated user"),
    ;

    private final String roleName;
    private final String name;
    private final String description;
}
