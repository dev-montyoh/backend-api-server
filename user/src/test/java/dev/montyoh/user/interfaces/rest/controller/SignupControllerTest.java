package dev.montyoh.user.interfaces.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.montyoh.user.application.commandservice.UserCreateCommandService;
import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.constants.StaticValues;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserSignupReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserSignupRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserSignupCommandMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSignupCommandMapper userSignupCommandMapper;

    @MockBean
    private UserCreateCommandService userCreateCommandService;

    @Test
    @DisplayName("회원가입 API 요청에 성공한다.")
    void signup_success() throws Exception {
        //  given
        UserSignupReqDto userSignupReqDto = UserSignupReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        UserCreateCommand userCreateCommand = UserCreateCommand.builder()
                .loginId(userSignupReqDto.loginId())
                .password(userSignupReqDto.password())
                .build();
        given(userSignupCommandMapper.mapToCommand(any())).willReturn(userCreateCommand);
        User user = User.builder()
                .userNo("testUserNo")
                .loginId(userSignupReqDto.loginId())
                .build();
        given(userCreateCommandService.create(any())).willReturn(user);
        UserSignupRspDto userSignupRspDto = UserSignupRspDto.builder()
                .userNo(user.getUserNo())
                .build();
        given(userSignupCommandMapper.mapToRspDto(any())).willReturn(userSignupRspDto);

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Signup.USER_SIGNUP_URL)
                                .content(objectMapper.writeValueAsString(userSignupReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(userSignupRspDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    @DisplayName("빈 아이디/짧은 비밀번호는 검증 실패로 400을 반환한다.")
    void signup_fail_invalid_input() throws Exception {
        //  given
        UserSignupReqDto invalidReqDto = UserSignupReqDto.builder()
                .loginId("")
                .password("short")
                .build();

        //  when,   then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(UserApiUrl.USER_V1_BASE_URL + UserApiUrl.Signup.USER_SIGNUP_URL)
                                .content(objectMapper.writeValueAsString(invalidReqDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string(StaticValues.HEADER_ERROR_CODE, ErrorCode.INVALID_INPUT.getCode()))
        ;
    }
}
