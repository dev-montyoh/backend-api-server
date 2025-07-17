package io.github.monty.api.user.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record UserLoginReqDto(String loginId, String password) {
}
