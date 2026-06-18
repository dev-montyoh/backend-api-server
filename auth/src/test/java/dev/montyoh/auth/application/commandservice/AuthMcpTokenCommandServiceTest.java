package dev.montyoh.auth.application.commandservice;

import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.domain.service.AuthCreateMcpTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthMcpTokenCommandServiceTest {

    @InjectMocks
    private AuthMcpTokenCommandService authMcpTokenCommandService;

    @Mock
    private AuthCreateMcpTokenService authCreateMcpTokenService;

    @Test
    @DisplayName("MCP 토큰 발급 애플리케이션 서비스를 호출한다.")
    void createMcpToken() {
        //  given
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(authCreateMcpTokenService.createMcpToken(anyString())).willReturn(authCreateMcpTokenVo);
        AuthCreateMcpTokenCommand command = AuthCreateMcpTokenCommand.builder()
                .userNo("testUserNo")
                .build();

        //  when
        AuthCreateMcpTokenVo actual = authMcpTokenCommandService.createMcpToken(command);

        //  then
        assertThat(actual.token()).isEqualTo(authCreateMcpTokenVo.token());
    }
}
