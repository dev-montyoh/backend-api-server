package dev.montyoh.user.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.user.application.commandservice.UserMcpLoginCommandService;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserMcpLoginCommandMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginMcpController.class)
class LoginMcpControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMcpLoginCommandMapper userMcpLoginCommandMapper;

    @MockBean
    private UserMcpLoginCommandService userMcpLoginCommandService;

    @Test
    @DisplayName("MCP 로그인 API 요청에 성공한다.")
    void login_success() throws Exception {
        //  given
        UserMcpLoginReqDto userMcpLoginReqDto = UserMcpLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        UserLoginCommand userLoginCommand = UserLoginCommand.builder()
                .loginId(userMcpLoginReqDto.loginId())
                .password(userMcpLoginReqDto.password())
                .build();
        given(userMcpLoginCommandMapper.mapToCommand(any())).willReturn(userLoginCommand);
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(userMcpLoginCommandService.login(any())).willReturn(authCreateMcpTokenVo);
        UserMcpLoginRspDto userMcpLoginRspDto = UserMcpLoginRspDto.builder()
                .token(authCreateMcpTokenVo.token())
                .build();
        given(userMcpLoginCommandMapper.mapToRspDto(any())).willReturn(userMcpLoginRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_MCP_LOGIN_URL)
                                .content(objectMapper.writeValueAsString(userMcpLoginReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userMcpLoginRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
