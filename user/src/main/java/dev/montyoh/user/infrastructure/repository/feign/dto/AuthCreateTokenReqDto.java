package dev.montyoh.user.infrastructure.repository.feign.dto;

import lombok.Builder;

@Builder
public record AuthCreateTokenReqDto(String userNo) {
}
