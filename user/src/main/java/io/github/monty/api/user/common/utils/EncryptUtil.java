package io.github.monty.api.user.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtil {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 평문을 암호화 한다.
     *
     * @param planeText 암호화 하고자 하는 평문
     * @return 암호화 결과
     */
    public static String encode(String planeText) {
        return bCryptPasswordEncoder.encode(planeText);
    }

    /**
     * 평문과 암호화된 텍스트를 비교하여 값이 일치하는지 확인한다.
     *
     * @param planeText   비교하고자 하는 평문
     * @param encodedText 암호화된 텍스트
     * @return 비교 결과
     */
    public static boolean match(String planeText, String encodedText) {
        return bCryptPasswordEncoder.matches(planeText, encodedText);
    }
}
