package io.github.monty.api.user.integration;

import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginRspDto;
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
        UserLoginReqDto userLoginReqDto = UserLoginReqDto.builder()
                .loginId(loginId)
                .password(password)
                .build();
        HttpEntity<UserLoginReqDto> requestHttpEntity = new HttpEntity<>(userLoginReqDto);

        //  when
        ResponseEntity<UserLoginRspDto> responseEntity = restTemplate.postForEntity(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_LOGIN_URL, requestHttpEntity, UserLoginRspDto.class);
        Optional<UserLoginRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertTrue(actual.isPresent()),
                () -> assertTrue(actual.map(UserLoginRspDto::accessToken).isPresent()),
                () -> assertTrue(actual.map(UserLoginRspDto::refreshToken).isPresent())
        );
    }

    @Test
    @DisplayName("로그인 요청을 했고, 없는 회원이라고 응답을 받는다.")
    void login_fail() {
        //  given
        String loginId = "testLoginId";
        String password = "testPassword";

        UserLoginReqDto userLoginReqDto = UserLoginReqDto.builder()
                .loginId(loginId)
                .password(password)
                .build();
        HttpEntity<UserLoginReqDto> requestHttpEntity = new HttpEntity<>(userLoginReqDto);

        //  when
        ResponseEntity<UserLoginRspDto> responseEntity = restTemplate.postForEntity(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_LOGIN_URL, requestHttpEntity, UserLoginRspDto.class);
        Optional<UserLoginRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }
}
