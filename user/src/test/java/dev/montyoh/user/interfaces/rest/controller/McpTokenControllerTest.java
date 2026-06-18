package dev.montyoh.user.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.user.application.commandservice.UserMcpTokenCommandService;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserMcpTokenCommandMapper;
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

@WebMvcTest(McpTokenController.class)
class McpTokenControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMcpTokenCommandMapper userMcpTokenCommandMapper;

    @MockBean
    private UserMcpTokenCommandService userMcpTokenCommandService;

    @Test
    @DisplayName("MCP 토큰 발급 API 요청에 성공한다.")
    void getMcpToken_success() throws Exception {
        //  given
        UserMcpTokenReqDto userMcpTokenReqDto = UserMcpTokenReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        UserLoginCommand userLoginCommand = UserLoginCommand.builder()
                .loginId(userMcpTokenReqDto.loginId())
                .password(userMcpTokenReqDto.password())
                .build();
        given(userMcpTokenCommandMapper.mapToCommand(any())).willReturn(userLoginCommand);
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(userMcpTokenCommandService.getMcpToken(any())).willReturn(authCreateMcpTokenVo);
        UserMcpTokenRspDto userMcpTokenRspDto = UserMcpTokenRspDto.builder()
                .token(authCreateMcpTokenVo.token())
                .build();
        given(userMcpTokenCommandMapper.mapToRspDto(any())).willReturn(userMcpTokenRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Mcp.MCP_TOKEN_URL)
                                .content(objectMapper.writeValueAsString(userMcpTokenReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userMcpTokenRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
