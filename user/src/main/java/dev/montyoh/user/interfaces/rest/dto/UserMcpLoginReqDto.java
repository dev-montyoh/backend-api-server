package dev.montyoh.user.interfaces.rest.dto;

import lombok.Builder;

@Builder
public record UserMcpLoginReqDto(String loginId, String password) {
}
