package io.github.monty.api.payment.common.utils;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class EncryptUtils {

    /**
     * 암호화 함수
     *
     * @param plainText   암호화 대상 평문
     * @param encryptType 암호화 알고리즘 타입
     * @return 암호화 결과
     */
    public static String encrypt(String plainText, EncryptType encryptType) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encryptType.getName());
            byte[] encodedText = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encodedText);
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException(ErrorCode.NOT_EXIST_ENCRYPT_ALGORITHM);
        }
    }
}
