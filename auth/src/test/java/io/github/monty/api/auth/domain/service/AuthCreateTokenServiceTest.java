package io.github.monty.api.auth.domain.service;

import io.github.monty.api.auth.common.constants.StaticValues;
import io.github.monty.api.auth.common.utils.JwtUtils;
import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.model.entity.UserRoleId;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.domain.repository.AuthRepository;
import io.github.monty.api.auth.domain.repository.RoleRepository;
import io.github.monty.api.auth.domain.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {""})
class AuthCreateTokenServiceTest {

    @InjectMocks
    private AuthCreateTokenService authCreateTokenService;

    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authCreateTokenService, "refreshKeyTtl", 10);
    }

    @Test
    @DisplayName("액세스 토큰 발급에 성공한다.")
    void createTokenAndSaveRefreshToken_success() {
        //  given
        Role role = Role.builder()
                .id(0)
                .name("ROLE_TEST")
                .build();

        List<UserRole> userRoleList = List.of(
                UserRole.builder()
                        .userRoleId(
                                UserRoleId.builder()
                                        .roleId(0)
                                        .userNo("0")
                                        .build()
                        )
                        .role(role)
                        .build()
        );
        given(userRoleRepository.findByUserRoleIdUserNo(anyString())).willReturn(userRoleList);
        given(roleRepository.findAllByIdIn(anyList())).willReturn(List.of(role));

        String accessToken = "TEST_ACCESS_TOKEN";
        String refreshToken = "TEST_REFRESH_TOKEN";
        given(jwtUtils.createAccessToken(anyString(), anyList())).willReturn(accessToken);
        given(jwtUtils.createRefreshToken(anyString())).willReturn(refreshToken);

        //  when
        AuthCreateTokenVo actual = authCreateTokenService.createTokenAndSaveRefreshToken("testUserNo");

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(accessToken),
                () -> assertThat(actual.refreshToken()).isEqualTo(refreshToken),
                () -> verify(authRepository, times(1)).findById(anyString())
        );
    }

    @Test
    @DisplayName("액세스 토큰과 리프레시 토큰을 재발급한다.")
    void refreshToken_success() {
        //  given
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("ZmF2bzEyMzQ1Njc4OWFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6MTIzNDU2Nzg5YQ=="));
        String userNo = "testUserNo";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        String parameterRefreshToken = Jwts.builder()
                .claims()
                .add(StaticValues.USER_NO, userNo)
                .and()
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(parameterRefreshToken);
        given(jwtUtils.parsingToken(any())).willReturn(claimsJws);

        Role role = Role.builder()
                .id(0)
                .name("ROLE_TEST")
                .build();
        List<UserRole> userRoleList = List.of(
                UserRole.builder()
                        .userRoleId(
                                UserRoleId.builder()
                                        .roleId(0)
                                        .userNo(userNo)
                                        .build()
                        )
                        .role(role)
                        .build()
        );
        given(userRoleRepository.findByUserRoleIdUserNo(anyString())).willReturn(userRoleList);
        given(roleRepository.findAllByIdIn(anyList())).willReturn(List.of(role));

        String accessToken = Jwts.builder()
                .claims()
                .add(StaticValues.USER_NO, userNo)
                .and()
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
        String refreshToken = Jwts.builder()
                .claims()
                .add(StaticValues.USER_NO, userNo)
                .and()
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
        given(jwtUtils.createAccessToken(anyString(), any())).willReturn(accessToken);
        given(jwtUtils.createRefreshToken(anyString(), any())).willReturn(refreshToken);

        //  when
        AuthRefreshTokenVo actual = authCreateTokenService.refreshToken(parameterRefreshToken);

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(accessToken),
                () -> assertThat(actual.refreshToken()).isEqualTo(refreshToken)
        );
    }
}