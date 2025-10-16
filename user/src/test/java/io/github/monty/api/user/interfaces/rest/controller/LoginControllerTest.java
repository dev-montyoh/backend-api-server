package io.github.monty.api.user.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monty.api.user.application.commandservice.MemberLoginCommandService;
import io.github.monty.api.user.domain.model.command.MemberLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginRspDto;
import io.github.monty.api.user.interfaces.rest.mapper.MemberLoginCommandMapper;
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

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberLoginCommandMapper memberLoginCommandMapper;

    @MockBean
    private MemberLoginCommandService memberLoginCommandService;

    @Test
    @DisplayName("로그인 API 요청에 성공한다.")
    void login_success() throws Exception {
        //  given
        MemberLoginReqDto memberLoginReqDto = MemberLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        MemberLoginCommand memberLoginCommand = MemberLoginCommand.builder()
                .loginId(memberLoginReqDto.loginId())
                .password(memberLoginReqDto.password())
                .build();
        given(memberLoginCommandMapper.mapToCommand(any())).willReturn(memberLoginCommand);
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(memberLoginCommandService.login(any())).willReturn(authCreateTokenVo);
        MemberLoginRspDto memberLoginRspDto = MemberLoginRspDto.builder()
                .accessToken(authCreateTokenVo.accessToken())
                .refreshToken(authCreateTokenVo.refreshToken())
                .build();
        given(memberLoginCommandMapper.mapToRspDto(any())).willReturn(memberLoginRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Login.USER_LOGIN_URL)
                                .content(objectMapper.writeValueAsString(memberLoginReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(memberLoginRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
}