package io.github.monty.api.auth.integration;

import io.github.monty.api.auth.common.constants.ErrorCode;
import io.github.monty.api.auth.common.constants.StaticValues;
import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthCreateTokenControllerTest extends WireMockServerTest {

    @Test
    @DisplayName("토큰 생성 요청을 했고, 성공 응답을 받는다.")
    void createToken_success() {
        //  given
        Role role = this.insertRoleData("testRole", "testRole");
        String userNo = "0000";
        insertUserRoleData(userNo, role);
        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo(userNo)
                .build();

        HttpEntity<AuthCreateTokenReqDto> requestHttpEntity = new HttpEntity<>(authCreateTokenReqDto);

        //  when
        ResponseEntity<AuthCreateTokenRspDto> responseEntity = restTemplate.postForEntity(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.AUTH_CREATE_TOKEN, requestHttpEntity, AuthCreateTokenRspDto.class);
        Optional<AuthCreateTokenRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertTrue(actual.map(AuthCreateTokenRspDto::accessToken).isPresent()),
                () -> assertTrue(actual.map(AuthCreateTokenRspDto::refreshToken).isPresent())
        );
    }

    @Test
    @DisplayName("토큰 생성 요청을 했고, 해당되는 회원번호의 유저데이터가 없어 에러가 발생한다.")
    void createToken_fail() {
        //  given
        String userNo = "not_found";
        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo(userNo)
                .build();

        HttpEntity<AuthCreateTokenReqDto> requestHttpEntity = new HttpEntity<>(authCreateTokenReqDto);

        //  when
        ResponseEntity<AuthCreateTokenRspDto> responseEntity = restTemplate.postForEntity(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.AUTH_CREATE_TOKEN, requestHttpEntity, AuthCreateTokenRspDto.class);
        Optional<AuthCreateTokenRspDto> actual = Optional.ofNullable(responseEntity.getBody());

        //  then
        assertAll(
                () -> assertThat(actual.isPresent()).isFalse(),
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(ErrorCode.NOT_FOUND_AUTH_INFO.getHttpStatus()),
                () -> assertThat(responseEntity.getHeaders().getFirst(StaticValues.HEADER_ERROR_CODE)).isEqualTo(ErrorCode.NOT_FOUND_AUTH_INFO.getCode())
        );
    }
}
