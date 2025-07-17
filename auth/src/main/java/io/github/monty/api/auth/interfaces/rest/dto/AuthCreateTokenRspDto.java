package io.github.monty.api.auth.interfaces.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AuthCreateTokenRspDto(String accessToken, String refreshToken) {
}
