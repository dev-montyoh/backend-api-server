package dev.montyoh.user.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record UserMcpTokenReqDto(String loginId, String password) {
}
