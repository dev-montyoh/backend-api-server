package io.github.monty.api.user.infrastructure.repository.feign.dto;

import lombok.Builder;

@Builder
public record AuthCreateTokenRspDto(String accessToken, String refreshToken) {
}
