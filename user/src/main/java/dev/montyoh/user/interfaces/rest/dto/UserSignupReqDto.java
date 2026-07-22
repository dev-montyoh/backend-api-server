package dev.montyoh.user.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserSignupReqDto(
        @NotBlank String loginId,
        @NotBlank @Size(min = 8) String password
) {
}
