package dev.montyoh.auth.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record AuthCreateTokenRspDto(String accessToken, String refreshToken) {
}
