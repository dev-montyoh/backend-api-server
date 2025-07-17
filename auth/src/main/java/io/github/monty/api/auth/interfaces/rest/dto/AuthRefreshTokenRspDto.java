package io.github.monty.api.auth.interfaces.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AuthRefreshTokenRspDto(String accessToken, String refreshToken) {
}
