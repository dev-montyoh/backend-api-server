package io.github.monty.api.user.integration;

import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginRspDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginControllerTest extends WireMockServerTest {

    @Test
    @DisplayName("로그인 요청을 했고 성공 응답을 받는다.")
    void login_success() {
        //  given
        String userNo = "0000";
        String loginId = "testLoginId";
        String password = "testPassword";
        this.insertUserData(userNo, loginId, password);
        MemberLoginReqDto memberLoginReqDto = MemberLoginReqDto.builder()
                .loginId(loginId)
                .password(password)
                .build();
        HttpEntity<MemberLoginReqDto> requestHttpEntity = new HttpEntity<>(memberLoginReqDto);

        //  when
        ResponseEntity<MemberLoginRspDto> responseEntity = restTemplate.postForEntity(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_LOGIN_URL, requestHttpEntity, MemberLoginRspDto.class);
        Optional<MemberLoginRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertTrue(actual.isPresent()),
                () -> assertTrue(actual.map(MemberLoginRspDto::accessToken).isPresent()),
                () -> assertTrue(actual.map(MemberLoginRspDto::refreshToken).isPresent())
        );
    }

    @Test
    @DisplayName("로그인 요청을 했고, 없는 회원이라고 응답을 받는다.")
    void login_fail() {
        //  given
        String loginId = "testLoginId";
        String password = "testPassword";

        MemberLoginReqDto memberLoginReqDto = MemberLoginReqDto.builder()
                .loginId(loginId)
                .password(password)
                .build();
        HttpEntity<MemberLoginReqDto> requestHttpEntity = new HttpEntity<>(memberLoginReqDto);

        //  when
        ResponseEntity<MemberLoginRspDto> responseEntity = restTemplate.postForEntity(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_LOGIN_URL, requestHttpEntity, MemberLoginRspDto.class);
        Optional<MemberLoginRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }
}
