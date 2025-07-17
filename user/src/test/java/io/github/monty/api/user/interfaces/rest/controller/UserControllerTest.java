package io.github.monty.api.user.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monty.api.user.application.commandservice.UserLoginCommandService;
import io.github.monty.api.user.domain.model.command.UserLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginRspDto;
import io.github.monty.api.user.interfaces.rest.mapper.UserLoginCommandMapper;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserLoginCommandMapper userLoginCommandMapper;

    @MockBean
    private UserLoginCommandService userLoginCommandService;

    @Test
    @DisplayName("로그인 API 요청에 성공한다.")
    void login_success() throws Exception {
        //  given
        UserLoginReqDto userLoginReqDto = UserLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        UserLoginCommand userLoginCommand = UserLoginCommand.builder()
                .loginId(userLoginReqDto.loginId())
                .password(userLoginReqDto.password())
                .build();
        given(userLoginCommandMapper.mapToCommand(any())).willReturn(userLoginCommand);
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(userLoginCommandService.login(any())).willReturn(authCreateTokenVo);
        UserLoginRspDto userLoginRspDto = UserLoginRspDto.builder()
                .accessToken(authCreateTokenVo.accessToken())
                .refreshToken(authCreateTokenVo.refreshToken())
                .build();
        given(userLoginCommandMapper.mapToRspDto(any())).willReturn(userLoginRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.LOGIN_URL)
                                .content(objectMapper.writeValueAsString(userLoginReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userLoginRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
}