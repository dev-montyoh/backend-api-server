package io.github.monty.api.payment.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EncryptType {
    SHA256("SHA-256"),
    SHA512("SHA-512");
    ;

    private final String name;
}
