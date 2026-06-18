package dev.montyoh.auth.domain.model.vo;

import lombok.Builder;

@Builder
public record AuthCreateMcpTokenVo(String token) {
}
