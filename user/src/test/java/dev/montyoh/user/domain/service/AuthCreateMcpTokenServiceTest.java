package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.domain.repository.AuthRepository;
import dev.montyoh.user.domain.repository.UserRepository;
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
class AuthCreateMcpTokenServiceTest {

    @InjectMocks
    private AuthCreateMcpTokenService authCreateMcpTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthRepository authRepository;

    @Test
    @DisplayName("MCP 토큰 생성 요청에 성공했다.")
    void getMcpToken_success() {
        //  given
        User user = User.builder()
                .userNo("testUserNo")
                .build();
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(user));
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(authRepository.createMcpToken(any())).willReturn(authCreateMcpTokenVo);

        //  when
        AuthCreateMcpTokenVo actual = authCreateMcpTokenService.getMcpToken("testLoginId");

        //  then
        assertThat(actual.token()).isEqualTo(authCreateMcpTokenVo.token());
    }

    @Test
    @DisplayName("회원 정보가 존재하지 않는다.")
    void getMcpToken_not_found_user_info() {
        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> authCreateMcpTokenService.getMcpToken("testLoginId"));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getMessage())
        );
    }
}
