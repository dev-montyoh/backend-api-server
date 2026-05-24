package dev.montyoh.auth.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record AuthCreateTokenReqDto(String userNo) {
}
