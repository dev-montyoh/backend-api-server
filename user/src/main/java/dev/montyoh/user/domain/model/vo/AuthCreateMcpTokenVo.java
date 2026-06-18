package dev.montyoh.user.domain.model.vo;

import lombok.Builder;

@Builder
public record AuthCreateMcpTokenVo(String token) {
}
