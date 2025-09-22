package io.github.monty.api.user.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record UserLoginRspDto(String accessToken, String refreshToken) {
}
