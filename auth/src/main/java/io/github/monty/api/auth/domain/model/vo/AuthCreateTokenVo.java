package io.github.monty.api.auth.domain.model.vo;

import lombok.Builder;

@Builder
public record AuthCreateTokenVo(String accessToken, String refreshToken) {
}
