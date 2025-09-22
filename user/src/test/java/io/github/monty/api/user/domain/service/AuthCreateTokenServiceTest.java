package io.github.monty.api.user.domain.service;

import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.domain.model.aggregate.User;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.domain.repository.AuthRepository;
import io.github.monty.api.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthCreateTokenServiceTest {
    @InjectMocks
    private AuthCreateTokenService authCreateTokenService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthRepository authRepository;

    @Test
    @DisplayName("토큰 정보 생성 요청에 성공했다.")
    void getUserNoAndCreateToken_success() {
        //  given
        User user = User.builder()
                .userNo("testUserNo")
                .build();
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(user));
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(authRepository.createAccessTokenAndRefreshToken(any())).willReturn(authCreateTokenVo);

        //  when
        AuthCreateTokenVo actual = authCreateTokenService.getTokens("testLoginId");

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(actual.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken())
        );
    }

    @Test
    @DisplayName("회원 정보가 존재하지 않는다.")
    void getUserNoAndCreateToken_not_found_user_info() {
        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> authCreateTokenService.getTokens("testLoginId"));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getMessage())
        );
    }
}