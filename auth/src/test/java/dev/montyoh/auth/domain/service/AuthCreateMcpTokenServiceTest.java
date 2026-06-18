package dev.montyoh.auth.domain.service;

import dev.montyoh.auth.common.utils.JwtUtils;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
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
class AuthCreateMcpTokenServiceTest {

    @InjectMocks
    private AuthCreateMcpTokenService authCreateMcpTokenService;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("MCP 토큰 발급에 성공한다.")
    void createMcpToken_success() {
        //  given
        String mcpToken = "testMcpToken";
        given(jwtUtils.createMcpToken(anyString())).willReturn(mcpToken);

        //  when
        AuthCreateMcpTokenVo actual = authCreateMcpTokenService.createMcpToken("testUserNo");

        //  then
        assertThat(actual.token()).isEqualTo(mcpToken);
    }
}
