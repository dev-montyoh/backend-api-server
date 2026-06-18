package dev.montyoh.auth.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.auth.application.commandservice.AuthMcpTokenCommandService;
import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.interfaces.rest.constants.AuthApiUrl;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenRspDto;
import dev.montyoh.auth.interfaces.rest.mapper.AuthCreateMcpTokenCommandMapper;
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
    private AuthMcpTokenCommandService authMcpTokenCommandService;

    @MockBean
    private AuthCreateMcpTokenCommandMapper authCreateMcpTokenCommandMapper;

    @Test
    @DisplayName("MCP 토큰 발급 요청을 했고 성공 응답이 왔다.")
    void createMcpToken_success() throws Exception {
        //  given
        AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto = AuthCreateMcpTokenReqDto.builder()
                .userNo("testUserNo")
                .build();
        AuthCreateMcpTokenCommand command = AuthCreateMcpTokenCommand.builder()
                .userNo(authCreateMcpTokenReqDto.userNo())
                .build();
        given(authCreateMcpTokenCommandMapper.mapToCommand(any())).willReturn(command);

        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();
        given(authMcpTokenCommandService.createMcpToken(any())).willReturn(authCreateMcpTokenVo);

        AuthCreateMcpTokenRspDto authCreateMcpTokenRspDto = AuthCreateMcpTokenRspDto.builder()
                .token(authCreateMcpTokenVo.token())
                .build();
        given(authCreateMcpTokenCommandMapper.mapToRspDto(any())).willReturn(authCreateMcpTokenRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.Mcp.CREATE_MCP_TOKEN)
                                .content(objectMapper.writeValueAsString(authCreateMcpTokenReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(authCreateMcpTokenRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
