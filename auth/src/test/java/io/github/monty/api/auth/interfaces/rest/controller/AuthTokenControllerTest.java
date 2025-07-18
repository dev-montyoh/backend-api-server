package io.github.monty.api.auth.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monty.api.auth.application.commandservice.AuthTokenCommandService;
import io.github.monty.api.auth.common.constants.CustomHeaders;
import io.github.monty.api.auth.domain.model.command.AuthCreateTokenCommand;
import io.github.monty.api.auth.domain.model.command.AuthRefreshTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthRefreshTokenRspDto;
import io.github.monty.api.auth.interfaces.rest.mapper.AuthCreateTokenCommandMapper;
import io.github.monty.api.auth.interfaces.rest.mapper.AuthRefreshTokenCommandMapper;
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

@WebMvcTest(AuthTokenController.class)
class AuthTokenControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthTokenCommandService authTokenCommandService;

    @MockBean
    private AuthCreateTokenCommandMapper authCreateTokenCommandMapper;

    @MockBean
    private AuthRefreshTokenCommandMapper authRefreshTokenCommandMapper;

    @Test
    @DisplayName("토큰 생성 요청을 했고 성공 응답이 왔다.")
    void createToken_success() throws Exception {
        //  given
        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo("testUserNo")
                .build();
        AuthCreateTokenCommand authCreateTokenCommand = AuthCreateTokenCommand.builder()
                .userNo(authCreateTokenReqDto.userNo())
                .build();
        given(authCreateTokenCommandMapper.mapToCommand(any())).willReturn(authCreateTokenCommand);

        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(authTokenCommandService.createToken(any())).willReturn(authCreateTokenVo);

        AuthCreateTokenRspDto authCreateTokenRspDto = AuthCreateTokenRspDto.builder()
                .accessToken(authCreateTokenVo.accessToken())
                .refreshToken(authCreateTokenVo.refreshToken())
                .build();
        given(authCreateTokenCommandMapper.mapToRspDto(any())).willReturn(authCreateTokenRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.Token.CREATE_TOKEN)
                                .content(objectMapper.writeValueAsString(authCreateTokenReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(authCreateTokenRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    @DisplayName("토큰 갱신 요청을 했고 성공 응답이 왔다.")
    void refreshToken_success() throws Exception {
        //  given
        String inputRefreshToken = "inputRefreshToken";
        AuthRefreshTokenCommand authRefreshTokenCommand = AuthRefreshTokenCommand.builder()
                .refreshToken(inputRefreshToken)
                .build();
        given(authRefreshTokenCommandMapper.mapToCommand(any())).willReturn(authRefreshTokenCommand);

        String outputAccessToken = "outputAccessToken";
        String outputRefreshToken = "outputRefreshToken";
        AuthRefreshTokenVo authRefreshTokenVo = AuthRefreshTokenVo
                .builder()
                .accessToken(outputAccessToken)
                .refreshToken(outputRefreshToken)
                .build();
        given(authTokenCommandService.refreshToken(any())).willReturn(authRefreshTokenVo);

        AuthRefreshTokenRspDto authRefreshTokenRspDto = AuthRefreshTokenRspDto
                .builder()
                .accessToken(outputAccessToken)
                .refreshToken(outputRefreshToken)
                .build();
        given(authRefreshTokenCommandMapper.mapToRspDto(any())).willReturn(authRefreshTokenRspDto);

        //  when,   then
        mockMvc.perform(
                MockMvcRequestBuilders.put(AuthApiUrl.AUTH_V1_BASE_URL + AuthApiUrl.Token.REFRESH_TOKEN)
                        .header(CustomHeaders.REFRESH_TOKEN, inputRefreshToken)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(authRefreshTokenRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                ;
    }
}