package io.github.monty.api.auth.common.utils;

import io.github.monty.api.auth.common.constants.StaticValues;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    private final SecretKey jwtSecretKey;

    @Value("${service.jwt.valid-time.access}")
    private long accessTokenValidTime;

    @Value("${service.jwt.valid-time.refresh}")
    private long refreshTokenValidTime;

    public JwtUtils(@Value("${service.jwt.secret-key}") String secretKey) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * 액세스 토큰 생성 후 반환
     *
     * @param userNo       회원 번호
     * @param userRoleList 유저 권한 리스트
     * @return 액세스 토큰
     */
    public String createAccessToken(String userNo, List<String> userRoleList) {
        return this.createToken(
                userNo,
                userRoleList,
                new Date(System.currentTimeMillis() + Duration.ofMinutes(accessTokenValidTime).toMillis())
        );
    }

    /**
     * 리프레시 토큰 생성 후 반환
     *
     * @param userNo 회원 번호
     * @return 리프레시 토큰
     */
    public String createRefreshToken(String userNo) {
        return this.createToken(
                userNo,
                null,
                new Date(System.currentTimeMillis() + Duration.ofMinutes(refreshTokenValidTime).toMillis())
        );
    }

    /**
     * 리프레시 토큰 생성 후 반환
     *
     * @param userNo         회원 번호
     * @param expirationDate 만료 일자
     * @return 리프레시 토큰
     */
    public String createRefreshToken(String userNo, Date expirationDate) {
        return this.createToken(userNo, null, expirationDate);
    }

    /**
     * 토큰 생성 후 반환
     *
     * @param userNo         토큰 생성 요청한 회원 번호
     * @param userRoleList   해당 회원이 가지고 있는 USER_ROLE 리스트
     * @param expirationDate 토큰의 만료 일자
     * @return 생성된 토큰
     */
    private String createToken(String userNo, List<String> userRoleList, Date expirationDate) {
        return Jwts.builder()
                .claims()
                .add(StaticValues.USER_NO, userNo)
                .add(StaticValues.USER_ROLES, userRoleList)
                .and()
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(jwtSecretKey)
                .compact()
                ;
    }

    /**
     * 전달 받은 토큰을 파싱한다.
     *
     * @param token JsonWebToken
     * @return Jws<Claims> 객체
     */
    public Jws<Claims> parsingToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token);
    }
}
