package dev.montyoh.user.application.commandservice;

import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.domain.service.AuthCreateMcpTokenService;
import dev.montyoh.user.domain.service.UserVerifyPasswordService;
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
class UserMcpTokenCommandServiceTest {

    @InjectMocks
    private UserMcpTokenCommandService userMcpTokenCommandService;

    @Mock
    private UserVerifyPasswordService userVerifyPasswordService;

    @Mock
    private AuthCreateMcpTokenService authCreateMcpTokenService;

    @Test
    @DisplayName("MCP 토큰 발급 애플리케이션 서비스 호출을 한다.")
    void getMcpToken() {
        //  given
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(authCreateMcpTokenService.getMcpToken(anyString())).willReturn(authCreateMcpTokenVo);
        UserLoginCommand userLoginCommand = UserLoginCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        AuthCreateMcpTokenVo actual = userMcpTokenCommandService.getMcpToken(userLoginCommand);

        //  then
        assertAll(
                () -> assertThat(actual.token()).isEqualTo(authCreateMcpTokenVo.token()),
                () -> verify(userVerifyPasswordService, times(1)).verifyPassword(anyString(), anyString())
        );
    }
}
