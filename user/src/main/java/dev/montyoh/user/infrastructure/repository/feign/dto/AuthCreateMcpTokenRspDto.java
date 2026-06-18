package dev.montyoh.user.infrastructure.repository.feign.dto;

import lombok.Builder;

@Builder
public record AuthCreateMcpTokenRspDto(String token) {
}
