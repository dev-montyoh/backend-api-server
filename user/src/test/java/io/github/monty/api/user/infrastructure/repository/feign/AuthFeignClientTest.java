package io.github.monty.api.user.infrastructure.repository.feign;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.infrastructure.repository.feign.constants.AuthUrl;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthFeignClientTest extends WireMockTest {

    @Autowired
    private AuthFeignClient authFeignClient;

    @Test
    @DisplayName("Auth 애플리케이션에게 토큰 생성 요청을 했고 성공했다.")
    void createAccessTokenAndRefreshToken_success() {
        //  given
        AuthCreateTokenRspDto authCreateTokenRspDto = AuthCreateTokenRspDto.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        setupSuccessResponse(RequestMethod.POST.value(), AuthUrl.AUTH_CREATE_TOKEN, authCreateTokenRspDto);

        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo("testUserNo")
                .build();

        //  when
        ResponseEntity<AuthCreateTokenRspDto> responseEntity = authFeignClient.createAccessTokenAndRefreshToken(authCreateTokenReqDto);

        //  then
        assertNotNull(responseEntity.getBody());
        AuthCreateTokenRspDto actual = responseEntity.getBody();
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(authCreateTokenRspDto.accessToken()),
                () -> assertThat(actual.refreshToken()).isEqualTo(authCreateTokenRspDto.refreshToken())
        );
    }

    @Test
    @DisplayName("Auth 애플리케이션에게 토큰 생성 요청을 했고 실패했다.")
    void createAccessTokenAndRefreshToken_fail() {
        //  given
        setupFailResponse(RequestMethod.POST.value(), AuthUrl.AUTH_CREATE_TOKEN, ErrorCode.INTERNAL_SERVER_ERROR);

        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo("testUserNo")
                .build();
        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> authFeignClient.createAccessTokenAndRefreshToken(authCreateTokenReqDto));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
        );
    }
}