package dev.montyoh.payment.common.utils;

import dev.montyoh.payment.common.constants.EncryptType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class EncryptUtilsTest {

    @Test
    @DisplayName("SHA-256 으로 암호화한다.")
    void encrypt_sha256() {
        //  given
        String plainText = "testPlainText";

        //  when
        String result = EncryptUtils.encrypt(plainText, EncryptType.SHA256);

        //  then
        assertAll(
                () -> assertThat(result).hasSize(64),
                () -> assertThat(result).isEqualTo(EncryptUtils.encrypt(plainText, EncryptType.SHA256))
        );
    }

    @Test
    @DisplayName("SHA-512 으로 암호화한다.")
    void encrypt_sha512() {
        //  given
        String plainText = "testPlainText";

        //  when
        String result = EncryptUtils.encrypt(plainText, EncryptType.SHA512);

        //  then
        assertAll(
                () -> assertThat(result).hasSize(128),
                () -> assertThat(result).isEqualTo(EncryptUtils.encrypt(plainText, EncryptType.SHA512))
        );
    }

    @Test
    @DisplayName("SHA-256 과 SHA-512 암호화 결과는 다르다.")
    void encrypt_sha256_and_sha512_produce_different_results() {
        //  given
        String plainText = "testPlainText";

        //  when, then
        assertThat(EncryptUtils.encrypt(plainText, EncryptType.SHA256))
                .isNotEqualTo(EncryptUtils.encrypt(plainText, EncryptType.SHA512));
    }
}
