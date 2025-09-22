package io.github.monty.api.user.application.commandservice;

import io.github.monty.api.user.domain.model.command.UserLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.domain.service.AuthCreateTokenService;
import io.github.monty.api.user.domain.service.UserVerifyPasswordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserLoginCommandServiceTest {
    @InjectMocks
    private UserLoginCommandService userLoginCommandService;

    @Mock
    private UserVerifyPasswordService userVerifyPasswordService;

    @Mock
    private AuthCreateTokenService authCreateTokenService;

    @Test
    @DisplayName("로그인 애플리케이션 서비스 호출을 한다.")
    void login() {
        //  given
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(authCreateTokenService.getTokens(anyString())).willReturn(authCreateTokenVo);
        UserLoginCommand userLoginCommand = UserLoginCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        AuthCreateTokenVo actual = userLoginCommandService.login(userLoginCommand);

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(actual.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken()),
                () -> verify(userVerifyPasswordService, times(1)).verifyPassword(anyString(), anyString())
        );
    }
}