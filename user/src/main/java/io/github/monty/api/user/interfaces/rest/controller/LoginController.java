package io.github.monty.api.user.interfaces.rest.controller;

import io.github.monty.api.user.application.commandservice.UserLoginCommandService;
import io.github.monty.api.user.domain.model.command.UserLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.constants.UserApiUrl;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginRspDto;
import io.github.monty.api.user.interfaces.rest.mapper.UserLoginCommandMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserApiUrl.USER_V1_BASE_URL)
@Tag(name = "User Login API", description = "로그인 관련 API")
public class LoginController {

    private final UserLoginCommandService userLoginCommandService;

    private final UserLoginCommandMapper userLoginCommandMapper;

    /**
     * 로그인
     * 토큰을 응답 값으로 전달받는다.
     *
     * @param userLoginReqDto 로그인 요청 Dto
     * @return 토큰 생성 결과 값을 담은 응답
     */
    @Operation(summary = "회원 로그인 API", description = "회원 로그인 처리를 한 후 해당 정보를 반환한다.")
    @PostMapping(value = UserApiUrl.Login.USER_LOGIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginRspDto> login(@RequestBody UserLoginReqDto userLoginReqDto) {
        UserLoginCommand userLoginCommand = userLoginCommandMapper.mapToCommand(userLoginReqDto);
        AuthCreateTokenVo authCreateTokenVo = userLoginCommandService.login(userLoginCommand);
        UserLoginRspDto userLoginRspDto = userLoginCommandMapper.mapToRspDto(authCreateTokenVo);
        return new ResponseEntity<>(userLoginRspDto, HttpStatus.OK);
    }
}
